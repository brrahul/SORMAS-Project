package de.symeda.sormas.backend.region;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.symeda.sormas.backend.common.AbstractAdoService;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.util.InfrastructureDataImporter;
import de.symeda.sormas.backend.util.InfrastructureDataImporter.DistrictConsumer;



@Stateless
@LocalBean
public class DistrictService extends AbstractAdoService<District> {
	
	public DistrictService() {
		super(District.class);
	}
	
	public List<District> getAllWithoutEpidCode() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<District> cq = cb.createQuery(getElementClass());
		Root<District> from = cq.from(getElementClass());
		cq.where(cb.isNull(from.get(District.EPID_CODE)));
		cq.orderBy(cb.asc(from.get(AbstractDomainObject.ID)));

		return em.createQuery(cq).getResultList();
	}
	
	public int getCountByRegion(Region region) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<District> from = cq.from(getElementClass());
		cq.select(cb.count(from));
		cq.where(cb.equal(from.get(District.REGION), region));
		
		return em.createQuery(cq).getSingleResult().intValue();
	}

	public District getByName(String name) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<District> cq = cb.createQuery(getElementClass());
		Root<District> from = cq.from(getElementClass());
	
		cq.where(cb.equal(from.get(District.NAME), name));
		try {
			return em.createQuery(cq).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public Predicate createUserFilter(CriteriaBuilder cb, CriteriaQuery cq, From<District, District> from, User user) {
		// no fitler by user needed
		return null;
	}	

	public void importDistricts(String countryName, List<Region> regions) {
		InfrastructureDataImporter.importDistricts(countryName, new DistrictConsumer() {
			
			private Region cachedRegion = null;
			
			@Override
			public void consume(String regionName, String districtName, String epidCode, Integer population, Float growthRate) {
					
				if (cachedRegion == null || !cachedRegion.getName().equals(regionName)) {
					Optional<Region> regionResult = regions.stream()
							.filter(r -> r.getName().equals(regionName))
							.findFirst();

					if (regionResult.isPresent()) {
						cachedRegion = regionResult.get();
					} else {
						logger.warn("Could not find region '" + regionName + "' for district '" + districtName + "'");
						return;
					}
					
					if (cachedRegion.getDistricts() == null) {
						cachedRegion.setDistricts(new ArrayList<District>());
					}
				}
				Optional<District> districtResult = cachedRegion.getDistricts().stream()
						.filter(r -> r.getName().equals(districtName))
						.findFirst();
				
				District district;
				if (districtResult.isPresent()) {
					district = districtResult.get();
				} else {
					district = new District();
					cachedRegion.getDistricts().add(district);
					district.setName(districtName);
					district.setRegion(cachedRegion);
				}
				
				district.setEpidCode(epidCode);
				district.setPopulation(population);
				district.setGrowthRate(growthRate);

				persist(district);
			}
		});
	}
}
