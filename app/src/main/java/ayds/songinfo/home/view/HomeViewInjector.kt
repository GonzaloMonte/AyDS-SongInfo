package ayds.songinfo.home.view

import ayds.songinfo.home.controller.HomeControllerInjector
import ayds.songinfo.home.model.HomeModelInjector
import ayds.songinfo.home.view.formatter.PrecisionFormatterFactoryImpl

object HomeViewInjector {

    val songDescriptionHelper: SongDescriptionHelper = SongDescriptionHelperImpl(PrecisionFormatterFactoryImpl)

    fun init(homeView: HomeView) {
        HomeModelInjector.initHomeModel(homeView)
        HomeControllerInjector.onViewStarted(homeView)
    }
}