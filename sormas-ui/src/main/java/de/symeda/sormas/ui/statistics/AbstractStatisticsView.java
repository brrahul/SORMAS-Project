/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.ui.statistics;

import com.vaadin.ui.Label;

import de.symeda.sormas.api.I18nProperties;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.CurrentUser;
import de.symeda.sormas.ui.SubNavigationMenu;
import de.symeda.sormas.ui.utils.AbstractSubNavigationView;

@SuppressWarnings("serial")
public class AbstractStatisticsView extends AbstractSubNavigationView {

	public final static String I18N_PREFIX = "Statistics";
	
	public static final String ROOT_VIEW_NAME = "statistics";
	
	protected AbstractStatisticsView(String viewName) {
		super(viewName);
	}

	@Override
	public void refreshMenu(SubNavigationMenu menu, Label infoLabel, Label infoLabelSub, String params) {
		menu.removeAllViews();
		menu.addView(StatisticsView.VIEW_NAME, I18nProperties.getPrefixFieldCaption(I18N_PREFIX, StatisticsView.VIEW_NAME), params);
		if (CurrentUser.getCurrent().hasUserRight(UserRight.DATABASE_EXPORT_ACCESS)) {
			menu.addView(DatabaseExportView.VIEW_NAME, I18nProperties.getPrefixFieldCaption(I18N_PREFIX, "database-export"), params);
		}
	
		hideInfoLabel();
	}
	
}
