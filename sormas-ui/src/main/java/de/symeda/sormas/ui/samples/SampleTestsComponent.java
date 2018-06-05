package de.symeda.sormas.ui.samples;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.sample.SampleReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.login.LoginHelper;
import de.symeda.sormas.ui.utils.CssStyles;

@SuppressWarnings("serial")
public class SampleTestsComponent extends VerticalLayout {

	private SampleTestGrid grid;

	private VerticalLayout gridLayout;

	public SampleTestsComponent(SampleReferenceDto sampleRef, boolean sampleReceived, int caseSampleCount) {
		Label headline = new Label("Sample tests");
		CssStyles.style(headline, CssStyles.H3);
		addComponent(headline);
		
		if (sampleReceived) {
			setSizeFull();
			addStyleName("crud-view");

			grid = new SampleTestGrid(sampleRef, caseSampleCount);
			grid.setHeightMode(HeightMode.ROW);

			gridLayout = new VerticalLayout();
			gridLayout.addComponent(createTopBar(sampleRef, caseSampleCount));
			gridLayout.addComponent(grid);
			gridLayout.setSpacing(false);
			gridLayout.setSizeFull();
			gridLayout.setExpandRatio(grid, 1);
			gridLayout.setStyleName("crud-main-layout");

			addComponent(gridLayout);
		} else {
			Label infoLabel = new Label("To add tests to this sample, it has to be marked as received first.");
			addComponent(infoLabel);
		}
	}

	public HorizontalLayout createTopBar(SampleReferenceDto sampleRef, int caseSampleCount) {
		HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setSpacing(true);
		topLayout.setWidth(100, Unit.PERCENTAGE);
		topLayout.addStyleName(CssStyles.VSPACE_3);

		// Bulk operation dropdown
		if (LoginHelper.hasUserRight(UserRight.PERFORM_BULK_OPERATIONS)) {
			topLayout.setWidth(100, Unit.PERCENTAGE);

			MenuBar bulkOperationsDropdown = new MenuBar();	
			MenuItem bulkOperationsItem = bulkOperationsDropdown.addItem("Bulk Actions", null);

			Command deleteCommand = selectedItem -> {
				ControllerProvider.getSampleTestController().deleteAllSelectedItems(grid.getSelectedRows(), new Runnable() {
					public void run() {
						grid.deselectAll();
						grid.reload();
					}
				});
			};
			bulkOperationsItem.addItem("Delete", FontAwesome.TRASH, deleteCommand);

			topLayout.addComponent(bulkOperationsDropdown);
			topLayout.setComponentAlignment(bulkOperationsDropdown, Alignment.TOP_RIGHT);
			topLayout.setExpandRatio(bulkOperationsDropdown, 1);
		}

		Button createButton = new Button("New result");
		createButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		createButton.setIcon(FontAwesome.PLUS_CIRCLE);
		createButton.addClickListener(e -> ControllerProvider.getSampleTestController().create(sampleRef, grid, caseSampleCount));
		topLayout.addComponent(createButton);
		topLayout.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);

		return topLayout;
	}

}