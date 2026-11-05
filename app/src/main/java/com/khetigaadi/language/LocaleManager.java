package com.khetigaadi.language;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;
import androidx.annotation.RequiresApi;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.util.Utils;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.N;
public class LocaleManager {
    private final SessionManager sessionManager;
    public LocaleManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    public Context setLocale(Context c) {
        return updateResources(c, getLanguage());
    }
    public Context setNewLocale(Context c,String language) {
        persistLanguage(language);
        return updateResources(c,language);
    }
    public String getLanguage() {
        return sessionManager.getLanguage();
    }
    @SuppressLint("ApplySharedPref")
    private void persistLanguage(String language) {
        // use commit() instead of apply(), because sometimes we kill the application process
        // immediately that prevents apply() from finishing
        sessionManager.setLanguage(language);
    }
//    private Context updateResources(Context context,String language) {
//        Locale locale = new Locale(language);
//        Locale.setDefault(locale);
//        Resources res = context.getResources();
//        Configuration config = new Configuration(res.getConfiguration());
//        if (Utils.isAtLeastVersion(N)) {
//            setLocaleForApi24(config, locale);
//            context = context.createConfigurationContext(config);
//        } else if (Utils.isAtLeastVersion(JELLY_BEAN_MR1)) {
//            config.setLocale(locale);
//            context = context.createConfigurationContext(config);
//        } else {
//            config.locale = locale;
//            res.updateConfiguration(config, res.getDisplayMetrics());
//        }
//        return context;
//    }

    private Context updateResources(Context context,String language) {
        @SuppressWarnings("deprecation") Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if(Utils.isAtLeastVersion(N)) {
            setLocaleForApi24(config, locale);
             context = context.createConfigurationContext(config);
        }
        else if(Utils.isAtLeastVersion(JELLY_BEAN_MR1)) {
            config.setLocale(locale);
             context = context.createConfigurationContext(config);
        }
        else {
            //noinspection deprecation
            config.locale = locale;
            //noinspection deprecation
            res.updateConfiguration(config,res.getDisplayMetrics());
        }
        //noinspection deprecation
        res.updateConfiguration(config,res.getDisplayMetrics());
              if(context instanceof android.app.Application) {
                   return context;
              }
              if(Utils.isAtLeastVersion(JELLY_BEAN_MR1)) {
                      return context.createConfigurationContext(config);
              }
              else {
                      return context;
              }
    }
    @RequiresApi(api = N)
    private void setLocaleForApi24(Configuration config,Locale target) {
        Set<Locale> set = new LinkedHashSet<>();
        // bring the target locale to the front of the list
        set.add(target);
        LocaleList all = LocaleList.getDefault();
        for(int i = 0; i < all.size(); i++) {
            // append other locales supported by the user
            set.add(all.get(i));
        }
        Locale[] locales = set.toArray(new Locale[0]);
        config.setLocales(new LocaleList(locales));
    }
    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        //noinspection deprecation
        return Utils.isAtLeastVersion(N) ? config.getLocales().get(0):config.locale;
    }
}