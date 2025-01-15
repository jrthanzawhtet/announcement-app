package com.jdc.spring.model.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jdc.spring.model.entity.District;
import com.jdc.spring.model.entity.State;
import com.jdc.spring.model.entity.Township;
import com.jdc.spring.model.input.DistrictDto;
import com.jdc.spring.model.input.StateDto;
import com.jdc.spring.model.repo.DistrictRepo;
import com.jdc.spring.model.repo.StateRepo;
import com.jdc.spring.model.repo.TownshipRepo;
import com.jdc.spring.utils.exceptions.ApiBusinessException;
import com.jdc.spring.utils.io.DataModificationResult;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;

@Service
@Transactional(readOnly = true)
public class LocationService {
	
	@Autowired
	private StateRepo stateRepo;
	@Autowired
	private DistrictRepo districtRepo;
	@Autowired
	private TownshipRepo townshipRepo;

	/*
	 * @Transactional public DataModificationResult<Integer> upload(MultipartFile
	 * file) {
	 * 
	 * try(var excelFile = WorkbookFactory.create(file.getInputStream())) {
	 * 
	 * var sheet = excelFile.getSheetAt(0);
	 * 
	 * var states = new LinkedHashMap<String, Map<String, List<String>>>();
	 * 
	 * for(var i = 1; i < sheet.getLastRowNum(); i ++) { var row = sheet.getRow(i);
	 * 
	 * if(row.getLastCellNum() == 3) { var stateName = row.getCell(0).toString();
	 * var districtName = row.getCell(1).toString(); var townshipName =
	 * row.getCell(2).toString();
	 * 
	 * var districts = states.get(stateName); if(null == districts) { districts =
	 * new LinkedHashMap<String, List<String>>(); states.put(stateName, districts);
	 * }
	 * 
	 * var townships = districts.get(districtName);
	 * 
	 * if(null == townships) { townships = new ArrayList<String>();
	 * districts.put(districtName, townships); }
	 * 
	 * townships.add(townshipName); } }
	 * 
	 * for(var stateName : states.keySet()) { var state = new State();
	 * state.setName(stateName); state = stateRepo.save(state);
	 * 
	 * var districts = states.get(stateName);
	 * 
	 * for(var districtName : districts.keySet()) { var district = new District();
	 * district.setName(districtName); district.setState(state); district =
	 * districtRepo.save(district);
	 * 
	 * var townships = districts.get(districtName);
	 * 
	 * for(var townshipName : townships) { var township = new Township();
	 * township.setName(townshipName); township.setDistrict(district);
	 * townshipRepo.save(township); } } }
	 * 
	 * var count = sheet.getLastRowNum() - 1; return new
	 * DataModificationResult<Integer>(count,
	 * "%d townships has been created.".formatted(count));
	 * 
	 * } catch (Exception e) { throw new ApiBusinessException(e.getMessage()); } }
	 * 
	 * public List<StateDto> getAllStates() { return
	 * stateRepo.findAll().stream().map(StateDto::from).toList(); }
	 * 
	 * public List<DistrictDto> searchDistrict(Optional<Integer> state,
	 * Optional<String> name) {
	 * 
	 * Function<CriteriaBuilder, CriteriaQuery<DistrictDto>> queryFun = cb -> { var
	 * cq = cb.createQuery(DistrictDto.class); var root = cq.from(District.class);
	 * 
	 * var where = new ArrayList<Predicate>();
	 * 
	 * // d.state.id = ? state.ifPresent(stateId ->
	 * where.add(cb.equal(root.get(District_.state).get(State_.id), stateId))); //
	 * lower(d.name) like ? name.ifPresent(param ->
	 * where.add(cb.like(cb.lower(root.get(District_.name)),
	 * param.toLowerCase().concat("%"))));
	 * 
	 * cq.where(where.toArray(i -> new Predicate[i]));
	 * 
	 * DistrictDto.select(cq, root);
	 * 
	 * cq.orderBy(cb.asc(root.get(District_.name)));
	 * 
	 * return cq; };
	 * 
	 * return districtRepo.search(queryFun); }
	 * 
	 * public List<TownshipDto> searchTownship(Optional<Integer> district,
	 * Optional<String> name) { Function<CriteriaBuilder,
	 * CriteriaQuery<TownshipDto>> queryFun = cb -> { var cq =
	 * cb.createQuery(TownshipDto.class); var root = cq.from(Township.class);
	 * 
	 * var where = new ArrayList<Predicate>();
	 * 
	 * // d.state.id = ? district.ifPresent(param ->
	 * where.add(cb.equal(root.get(Township_.district).get(District_.id), param)));
	 * // lower(d.name) like ? name.ifPresent(param ->
	 * where.add(cb.like(cb.lower(root.get(Township_.name)),
	 * param.toLowerCase().concat("%"))));
	 * 
	 * cq.where(where.toArray(i -> new Predicate[i]));
	 * 
	 * TownshipDto.select(cq, root);
	 * 
	 * cq.orderBy(cb.asc(root.get(Township_.name)));
	 * 
	 * return cq; };
	 * 
	 * return townshipRepo.search(queryFun); }
	 */
}
