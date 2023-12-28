package com.uasmobcom.catatanku

import androidx.compose.ui.test.junit4.ComposeContentTestRule

open class Page {
    companion object {
        inline fun <reified T : Page> on(composeTestRule: ComposeContentTestRule): T {
	        return Page().on(composeTestRule)
	    }
    }

    inline fun <reified T : Page> on(composeTestRule: ComposeContentTestRule): T {
        val page = T::class.constructors.first().call()
	    page.verify(composeTestRule)
        return page
    }

    open fun verify(composeTestRule: ComposeContentTestRule): Page {
        // Implement verification logic common to all pages, if any
        return this
    }
}