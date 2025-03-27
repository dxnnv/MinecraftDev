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

package com.demonwav.mcdev.creator.step

import com.intellij.ide.wizard.AbstractNewProjectWizardStep
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.Panel

abstract class AbstractCollapsibleStep(parent: NewProjectWizardStep) : AbstractNewProjectWizardStep(parent) {
    private val child by lazy { createStep() }

    abstract val title: String

    protected abstract fun createStep(): NewProjectWizardStep

    override fun setupUI(builder: Panel) {
        with(builder) {
            collapsibleGroup(title) {
                child.setupUI(this)
            }
        }
    }

    override fun setupProject(project: Project) {
        child.setupProject(project)
    }
}
