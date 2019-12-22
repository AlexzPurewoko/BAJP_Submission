package id.apwdevs.app.motvcatalogue.dataset

import id.apwdevs.app.motvcatalogue.model.DataModel


interface FakeShortAboutData {
    fun getExpectedShortData() : ArrayList<DataModel>
}