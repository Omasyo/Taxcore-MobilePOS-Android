package online.taxcore.pos

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.pax.dal.IDAL
import com.pax.neptunelite.api.NeptuneLiteUser
import com.example.lc_print_sdk.PrintUtil
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.realm.Realm
import io.realm.RealmConfiguration
import online.taxcore.pos.di.DaggerAppComponent
import javax.inject.Inject


class TaxCoreApp : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector

    companion object {
        lateinit var application: Context

        private var _dal: IDAL? = null

        val dal
            get(): IDAL {
                if (_dal == null) {
                    try {
                        val start = System.currentTimeMillis()
                        _dal =
                            NeptuneLiteUser.getInstance().getDal(application)
                        Log.i(
                            "Test",
                            "get dal cost:" + (System.currentTimeMillis() - start) + " ms"
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(
                            application,
                            "error occurred,DAL is null.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                return _dal!!
            }
    }

    override fun onCreate() {
        super.onCreate()
        initCalligraphy()
        initDagger()
        initRealm()

        application = applicationContext
        _dal = dal

        PrintUtil.getInstance(applicationContext)
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .name("taxcore3.realm")
            .build()

        Realm.setDefaultConfiguration(config)
    }

    private fun initDagger() {
        DaggerAppComponent.builder()
            .application(this).build().inject(this)
    }

    private fun initCalligraphy() {
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/roboto_regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
    }
}
