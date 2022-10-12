package translator.text.all.languagetranslator.voice.translation;

import android.content.Context;

public class Languages {
    private static final String[] speakLangCode = {"af-ZA", "am-ET", "ar", "hy-AM", "az-AZ", "eu-ES", "bn", "bg-BG", "ca-ES", "zh", "hr-HR", "cs-CZ", "da-DK", "nl-NL", "en", "fi-FI", "fr-FR", "gl-ES", "ka-GE", "de-DE", "el-GR", "gu-IN", "he-IL", "hi-IN", "hu-HU", "is-IS", "id-ID", "it-IT", "ja-JP", "jv-ID", "kn-IN", "ko-KR", "lv-LV", "lt-LT", "ms-MY", "ml-IN", "mr-IN", "ne-NP", "nb-NO", "fa-IR", "pl-PL", "pt", "ro-RO", "ru-RU", "sr-RS", "si-LK", "sk-SK", "sl-SI", "es", "su-ID", "sw", "sv-SE", "ta", "te-IN", "th-TH", "tr-TR", "uk-UA", "ur-IN", "vi-VN"};
    private static final String[] sptrCode = {"af", "am", "ar", "hy", "az", "eu", "bn", "bg", "ca", "zh", "hr", "cs", "da", "nl", "en", "fi", "fr", "gl", "ka", "de", "el", "gu", "he", "hi", "hu", "is", "id", "it", "ja", "jv", "kn", "ko", "lv", "lt", "ms", "ml", "mr", "ne", "no", "fa", "pl", "pt", "ro", "ru", "sr", "si", "sk", "sl", "es", "su", "sw", "sv", "ta", "te", "th", "tr", "uk", "ur", "vi"};

    public static String[] getSpeakLang(Context context) {
        return context.getResources().getStringArray(R.array.arraySpeakLanguages);
    }

    public static String getsptrCode(int i) {
        return sptrCode[i];
    }

    public static String getSpeakLangCode(int i) {
        return speakLangCode[i];
    }
}
