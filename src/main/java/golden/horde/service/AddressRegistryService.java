package golden.horde.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import golden.horde.domain.Address;
import golden.horde.domain.dto.AddressSearchResult;
import golden.horde.domain.dto.StreetDto;
import golden.horde.matcher.EditionalPrescription;
import golden.horde.matcher.LevenshteinMatcher;
import golden.horde.repository.AddressRepository;
import golden.horde.repository.StreetRepository;
import golden.horde.utils.UsefulUtils;

@Service
public class AddressRegistryService {

	private StreetRepository streetRepository;
	
	private AddressRepository addressRepository;
	
	private Map<String, List<StreetInfo>> mapByFirstLetter;
	
	private static final Logger logger = LoggerFactory.getLogger(AddressRegistryService.class);

	public AddressRegistryService(StreetRepository streetRepository, AddressRepository addressRepository) {
		this.streetRepository = streetRepository;
		this.addressRepository = addressRepository;
		initialize();
	}

	public String updateCache() {
		try {
			initialize();
			return "OK";
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
	}
	
	public String findMatches(String sourceSample) {
		String sample = UsefulUtils.checkTypos(sourceSample);
		String maybeSample = sample.equals(sourceSample) ? "" : sample;
		String empty = _serializeToJson(new AddressSearchResult(sourceSample, maybeSample, Collections.emptyList()));
		try {
			// парсим входную строку, выделяем улицы и дома
			List<String> streetSamples = new ArrayList<>();
			List<String> housesSamples = new ArrayList<>();
			if (!parseInputAddress(sample, streetSamples, housesSamples))
				return empty;
			// ищем все подходящие улицы
			Set<StreetInfo> streetSet = new HashSet<>();
			for (String streetSample : streetSamples) {
				streetSet.addAll(findStreets(streetSample));
			}
			if (streetSet.isEmpty())
				return empty;
			// преобразовываем улицы в DTO
			List<StreetDto> list = new ArrayList<>();			
			for(StreetInfo si : streetSet) {
				StreetDto dto = new StreetDto(si);
				// дома для улицы извлекаем всегда из валидного потомка, если есть
				StreetInfo current = si.successor == null ? si : si.successor;
				for(String houseSample : housesSamples) {
					for(String adr : current.adresses ) {
						if (adr.startsWith(houseSample)) {
							dto.getHouses().add(adr);
						}
					}
				}
				list.add(dto);
			}
			list.sort((o1, o2) -> o1.getNameRus().compareTo(o2.getNameRus()));
			
			AddressSearchResult result = new AddressSearchResult(sourceSample, maybeSample, list);
			return _serializeToJson(result);
		} catch (Exception exc) {
			return "error";
		}
	}
	
	private boolean parseInputAddress (String sample, List<String> streetSamples, List<String> housesSamples) {
		String[] splitted = sample.trim().toLowerCase().split("[\\s-()]");
		for(String part : splitted) {
			if (isStreetName(part)) {
				streetSamples.add(part);
			}
			else if (isHouseNumber(part)) {
				housesSamples.add(getHouseNumber(part));
			}
		}
		return !streetSamples.isEmpty();
	}
	
	private boolean isStreetName(String src) {
		if ((src.length() < 3) || Character.isDigit(src.charAt(0)))
			return false;
		return true;
	}
	
	private boolean isHouseNumber(String src) {
		return src.length() > 0 && src.length() < 5 && Character.isDigit(src.charAt(0));
	}
	
	private String getHouseNumber(String src) {
		char[] chars = src.toCharArray();
		String result = "";
		for(char ch : chars) {
			if (!Character.isDigit(ch)) {
				break;
			}
			result += ch;
		}
		return result;
	}
	
	
	private List<StreetInfo> findStreets(String sample) {
		// образец для поиска должен содержать минимум три буквы
		if (sample == null)
			return Collections.emptyList();
		sample = sample.trim().toLowerCase();
		if (sample.length() < 3)
			return Collections.emptyList();	
		// определяем список улиц для поиска по первой букве образца
		String firstLetter = sample.substring(0, 1);
		if (!mapByFirstLetter.keySet().contains(firstLetter))
			return Collections.emptyList();
		// Начинаем поиск
		List<StreetInfo> streets = mapByFirstLetter.get(firstLetter);
		// ищем подходящие улицы - сперва по подстроке...		
		List<StreetInfo> list = findByCoincidence(streets, sample);
		// ... а если не нашли, то пытаемся угадать
		if (list.isEmpty()) {
			list = findByHeuristic(streets, sample);
		}
		return list;
	}

	private void initialize() {
		// Загружаем улицы из БД
		List<StreetInfo> streetInfos = streetRepository.findAll()
													   .stream()
													   .map(StreetInfo::new)
													   .sorted((s1, s2) -> s1.ukrName.compareTo(s2.ukrName))
													   .collect(Collectors.toList());
		
		logger.info("Загружено улиц: " + streetInfos.size());
		// сортируем по первым буквам
		mapByFirstLetter = new HashMap<>();
		for (StreetInfo street : streetInfos) {
			for(String prt : street.allParts) {
				String firstLetter = prt.substring(0, 1);
				if (!mapByFirstLetter.keySet().contains(firstLetter)) {
					mapByFirstLetter.put(firstLetter, new ArrayList<>());
				}
				List<StreetInfo> list = mapByFirstLetter.get(firstLetter);
				if (!list.contains(street)) {
					list.add(street);
				}
			}
		}	
		// добавляем улицы предки
		mapByFirstLetter.values().stream().flatMap(Collection::stream).forEach(i -> i.successor = findSuccessors(i));	
		// загружаем адреса из БД
		List<Address> addresses = addressRepository.findAll();	
		logger.info("Загружено адресов: " + addresses.size());
		// добавляем адреса
		mapByFirstLetter.values().stream().flatMap(Collection::stream).forEach(i -> initializeByAddress(i, addresses));	
		logger.info("Инициализация завершена");
	}
	
	private void initializeByAddress(StreetInfo streetInfo, List<Address> addresses) {
		streetInfo.adresses = addresses.stream()
									   .filter(a -> a.getStreetId() == streetInfo.id)
									   .map(a -> a.getNumber() + "" + (a.getSuffix() == null ? "" : a.getSuffix()))
									   .collect(Collectors.toSet());
	}
	
	private String _serializeToJson(Object source) {
		try {
			return new ObjectMapper().writeValueAsString(source);
		} catch (Exception e) {
			return "error";
		}
	}
	
	private StreetInfo findSuccessors(StreetInfo streetInfo) {
		if ((streetInfo == null) || streetInfo.isValid)
			return null;
		List<StreetInfo> generation = new ArrayList<>();
		generation.add(streetInfo);
		do {			
			List<Integer> ids = generation.stream().map(i-> i.id).collect(Collectors.toList());
			generation = mapByFirstLetter.values()
										 .stream()
										 .flatMap(Collection::stream)
										 .filter(i -> ids.contains(i.previousId) || ids.contains(i.previousAnotherId))
										 .collect(Collectors.toList());
			Optional<StreetInfo> succeccor = generation.stream().filter(i->i.isValid).findFirst();
			if (succeccor.isPresent())
			{
				return succeccor.get();
			}
			
		} while (!generation.isEmpty());
		// unavoidable code
		return null;
	}
	
	private List<StreetInfo> findByHeuristic(List<StreetInfo> streets, final String sample) {		
		List<StreetInfo> result = new ArrayList<>();
		List<StreetInfo> threes = new ArrayList<>();
		List<StreetInfo> fours = new ArrayList<>();
		for (StreetInfo si : streets) {
			int distance = Integer.MAX_VALUE;
			for (String prt : si.allParts) {
				EditionalPrescription prescription = LevenshteinMatcher.compare(prt, sample);
				if (distance > prescription.distance) {
					distance = prescription.distance;
				}
			}
			if (distance <= 2) {
				result.add(si);
			}
			else if (distance == 3) { threes.add(si); }
			else if (distance == 4) { fours.add(si); }
		}
		int max = result.size();
		if (result.isEmpty()) {
			result.addAll(threes);
			result.addAll(fours);
			max = 5;
		}
		return result.stream().distinct().limit(max).collect(Collectors.toList());
	}
	
	private List<StreetInfo> findByCoincidence(List<StreetInfo> streets, final String sample) {
		// Ищем на точное совпадение
		List<StreetInfo> result = filter(streets, si -> si.rusName.equals(sample) || si.ukrName.equals(sample));
		// Ищем на частичное совпадение с начала любого слова в названии
		if (result.isEmpty()) {
			result = filter(streets, si ->  Arrays.stream(si.allParts).anyMatch(i -> i.startsWith(sample)));
		}
		return result;
	}
	
	
	private List<StreetInfo> filter (List<StreetInfo> streets, Predicate<StreetInfo> predicate) {
		return streets.stream().distinct().filter(predicate).collect(Collectors.toList());
	}
	

}

