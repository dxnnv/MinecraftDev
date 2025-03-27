/*
 * Minecraft Development for IntelliJ
 *
 * https://mcdev.io/
 *
 * Copyright (C) 2025 minecraft-dev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, version 3.0 only.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.demonwav.mcdev.insight.generation.ui

import com.demonwav.mcdev.insight.generation.GenerationData
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.psi.PsiClass
import javax.swing.JPanel

/**
 * Base class for the more-info event generation panel, to be overridden by the platforms. By default this class does nothing, shows no
 * panel, does no validation, and gathers no data.
 */
open class EventGenerationPanel(val chosenClass: PsiClass) {

    /**
     * Return the base panel for this dialog. This should be the root display element, and it should contain whatever JComponents are needed
     * by the platform. If this method returns null then no panel will be shown.

     * @return The main panel to display, or null if nothing should be displayed.
     */
    open val panel: JPanel?
        get() = null

    /**
     * This is called when the dialog is closing from the OK action. The platform should fill in their [GenerationData] object as
     * needed for whatever information their panel provides. The state of the panel can be assumed to be valid, since this will only be
     * called if [doValidate] has passed successfully.

     * @return The [GenerationData] object which will be passed to the
     * [EventListenerGenerationSupport][com.demonwav.mcdev.insight.generation.EventListenerGenerationSupport]
     */
    open fun gatherData(): GenerationData? {
        return null
    }

    /**
     * Validate the user input in the panel. Returns null when there are no errors, otherwise returns a [ValidationInfo] object filled
     * in with whatever relevant error data is necessary.

     * @return The relevant [ValidationInfo] object, or null if there are no errors.
     */
    open fun doValidate(): ValidationInfo? {
        return null
    }
}
