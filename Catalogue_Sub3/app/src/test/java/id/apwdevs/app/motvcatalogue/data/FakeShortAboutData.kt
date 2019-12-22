package id.apwdevs.app.motvcatalogue.data

import id.apwdevs.app.motvcatalogue.model.DataModel


interface FakeShortAboutData {
    fun getExpectedShortData() : ArrayList<DataModel>
}