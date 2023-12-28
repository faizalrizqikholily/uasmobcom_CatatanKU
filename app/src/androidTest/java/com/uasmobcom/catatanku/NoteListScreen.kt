package com.uasmobcom.catatanku

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

open class NoteListScreen: Page() {

    override fun verify(composeTestRule: ComposeContentTestRule): NoteListScreen {
        composeTestRule.onNodeWithTag("Note List Screen").assertIsDisplayed()
        return this
    }

    fun isDisplayed(composeTestRule: ComposeContentTestRule, testTag: String): NoteListScreen {
        composeTestRule.onNodeWithTag(testTag, true).assertIsDisplayed()
        return this
    }

    fun isClickable(composeTestRule: ComposeContentTestRule, testTag: String): NoteListScreen {
        composeTestRule.onNodeWithTag(testTag, true).assertHasClickAction()
        return this
    }

    fun click(composeTestRule: ComposeContentTestRule, testTag: String): NoteListScreen {
        composeTestRule.onNodeWithTag(testTag, true).performClick()
        composeTestRule.waitForIdle()
        return this
    }
}