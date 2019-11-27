package de.symeda.sormas.api.feature;

import java.util.Date;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.region.DistrictReferenceDto;
import de.symeda.sormas.api.region.RegionReferenceDto;

public class FeatureConfigurationDto extends EntityDto {

	private static final long serialVersionUID = 4027927530101427321L;
	
	public static final String FEATURE_TYPE = "featureType";
	public static final String REGION = "region";
	public static final String DISTRICT = "district";
	public static final String DISEASE = "disease";
	public static final String END_DATE = "endDate";
	
	private FeatureType featureType;
	private RegionReferenceDto region;
	private DistrictReferenceDto district;
	private Disease disease;
	private Date endDate;
	
	public FeatureType getFeatureType() {
		return featureType;
	}
	
	public void setFeatureType(FeatureType featureType) {
		this.featureType = featureType;
	}
	
	public RegionReferenceDto getRegion() {
		return region;
	}

	public void setRegion(RegionReferenceDto region) {
		this.region = region;
	}

	public DistrictReferenceDto getDistrict() {
		return district;
	}
	
	public void setDistrict(DistrictReferenceDto district) {
		this.district = district;
	}
	
	public Disease getDisease() {
		return disease;
	}
	
	public void setDisease(Disease disease) {
		this.disease = disease;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
