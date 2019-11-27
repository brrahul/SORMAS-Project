package de.symeda.sormas.ui.configuration.linelisting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.feature.FeatureConfigurationCriteria;
import de.symeda.sormas.api.feature.FeatureConfigurationDto;
import de.symeda.sormas.api.feature.FeatureConfigurationIndexDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.configuration.AbstractConfigurationView;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

@SuppressWarnings("serial")
public class LineListingConfigurationRegionView extends AbstractConfigurationView {

	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/linelisting";

	private VerticalLayout contentLayout;
	private LineListingAddDiseaseLayout addDiseaseLayout;
	private VerticalLayout lineListingConfigurationsLayout;

	public LineListingConfigurationRegionView() {
		super(VIEW_NAME);

		contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		contentLayout.setSpacing(true);
		contentLayout.setWidth(100, Unit.PERCENTAGE);
		contentLayout.setStyleName("crud-main-layout");

		buildView();

		addComponent(contentLayout);
	}

	private void buildView() {
		Label infoTextLabel = new Label(VaadinIcons.INFO_CIRCLE.getHtml() + " " + I18nProperties.getString(Strings.infoLineListingConfigurationRegion),
				ContentMode.HTML);
		CssStyles.style(infoTextLabel, CssStyles.LABEL_MEDIUM, CssStyles.VSPACE_4);
		contentLayout.addComponent(infoTextLabel);

		lineListingConfigurationsLayout = new VerticalLayout();
		lineListingConfigurationsLayout.setSpacing(true);
		lineListingConfigurationsLayout.setMargin(false);
		contentLayout.addComponent(lineListingConfigurationsLayout);
	
		// Retrieve existing line listing configurations from the database
		List<Disease> diseases = new ArrayList<>();
		diseases.addAll(Arrays.asList(Disease.values()));
		
		List<FeatureConfigurationIndexDto> lineListingConfigurations = FacadeProvider.getFeatureConfigurationFacade().getFeatureConfigurations(
				new FeatureConfigurationCriteria().featureType(FeatureType.LINE_LISTING).region(UserProvider.getCurrent().getUser().getRegion()),
				false);
		
		for (FeatureConfigurationIndexDto configuration : lineListingConfigurations) {
			
		}
		
		addDiseaseLayout = new LineListingAddDiseaseLayout(diseases);
		addDiseaseLayout.setWidth(600, Unit.PIXELS);

		addDiseaseLayout.setAddDiseaseCallback(disease -> {
			LineListingDiseaseLayout diseaseLayout = createDiseaseLayout(disease);
			lineListingConfigurationsLayout.addComponent(diseaseLayout);
			addDiseaseLayout.removeDiseaseFromList(disease);
		});

		contentLayout.addComponent(addDiseaseLayout);
	}

	private void showConfirmDisableAllWindow(LineListingDiseaseLayout diseaseLayout, Disease disease) {
		VaadinUiUtil.showConfirmationPopup(I18nProperties.getString(Strings.headingDisableLineListing), new Label(I18nProperties.getString(Strings.confirmationDisableAllLineListingRegion)), 
				I18nProperties.getCaption(Captions.actionConfirm), I18nProperties.getCaption(Captions.actionCancel), 
				480, result -> {
					if (Boolean.TRUE.equals(result)) {
						lineListingConfigurationsLayout.removeComponent(diseaseLayout);
						addDiseaseLayout.addDiseaseToList(disease);
						Notification.show(null, I18nProperties.getString(Strings.messageLineListingDisabled), Type.TRAY_NOTIFICATION);
					}
				});
	}
	
	private LineListingDiseaseLayout createDiseaseLayout(Disease disease) {
		LineListingDiseaseLayout diseaseLayout = new LineListingDiseaseLayout(disease);
		diseaseLayout.setWidth(300, Unit.PIXELS);
		CssStyles.style(diseaseLayout, CssStyles.VSPACE_4);

		diseaseLayout.setEditCallback(() -> {
			Window editWindow = VaadinUiUtil.createPopupWindow();
			FeatureConfigurationCriteria criteria = new FeatureConfigurationCriteria().disease(disease).region(UserProvider.getCurrent().getUser().getRegion()).featureType(FeatureType.LINE_LISTING);
			LineListingConfigurationEditLayout editLayout = new LineListingConfigurationEditLayout(FacadeProvider.getFeatureConfigurationFacade().getFeatureConfigurations(criteria, true));
			editWindow.setWidth(1024, Unit.PIXELS);
			editWindow.setCaption(I18nProperties.getString(Strings.headingEditLineListing));
			editWindow.setContent(editLayout);
			UI.getCurrent().addWindow(editWindow);
		});
		
		diseaseLayout.setDisableAllCallback(() -> {
			showConfirmDisableAllWindow(diseaseLayout, disease);
		});
		
		return diseaseLayout;
	}

}
