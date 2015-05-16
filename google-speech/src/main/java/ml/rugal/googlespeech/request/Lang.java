package ml.rugal.googlespeech.request;

/**
 * This enumerator is for declaring language. checked on 2015/05/16 with respect
 * to https://www.google.com/intl/en/chrome/demos/speech.html, line 196.
 * Google language content might change, if so, please verify the change and
 * raise an issue.
 *
 * @author Rugal Bernstein
 * @since 0.1
 */
public enum Lang
{

    Afrikaans("af-ZA"),
    Bahasa_Indonesia("id-ID"),
    Bahasa_Melayu("ms-MY"),
    Català("ca-ES"),
    Čeština("cs-CZ"),
    Dansk("da-DK"),
    Deutsch("de-DE"),
    Australia("en-AU"),
    Canada("en-CA"),
    India("en-IN"),
    New_Zealand("en-NZ"),
    South_Africa("en-ZA"),
    United_Kingdom("en-GB"),
    United_States("en-U"),
    Argentina("es-AR"),
    Bolivia("es-BO"),
    Chile("es-CL"),
    Colombia("es-CO"),
    Costa_Rica("es-CR"),
    Ecuador("es-EC"),
    El_Salvador("es-SV"),
    España("es-ES"),
    Estados_Unidos("es-US"),
    Guatemala("es-GT"),
    Honduras("es-HN"),
    México("es-MX"),
    Nicaragua("es-NI"),
    Panamá("es-PA"),
    Paraguay("es-PY"),
    Perú("es-PE"),
    Puerto_Rico("es-PR"),
    República_Dominicana("es-DO"),
    Uruguay("es-UY"),
    Venezuela("es-VE"),
    Euskara("eu-ES"),
    Filipino("fil-PH"),
    Français("fr-FR"),
    Galego("gl-ES"),
    Hrvatski("hr-HR"),
    IsiZulu("zu-ZA"),
    Íslenska("is-IS"),
    Italia("it-IT"),
    Svizzera("it-CH"),
    Lietuvių("lt-LT"),
    Magyar("hu-HU"),
    Nederlands("nl-NL"),
    Norsk_bokmål("nb-NO"),
    Polski("pl-PL"),
    Brasil("pt-BR"),
    Portugal("pt-PT"),
    Română("ro-RO"),
    Slovenščina("sl-SI"),
    Slovenčina("sk-SK"),
    Suomi("fi-FI"),
    Svenska("sv-SE"),
    TiếngViệt("vi-VN"),
    Türkçe("tr-TR"),
    Ελληνικά("el-GR"),
    български("bg-BG"),
    Pусский("ru-RU"),
    Српски("sr-RS"),
    Українська("uk-UA"),
    한국어("ko-KR"),
    普通话_中国大陆("cmn-Hans-CN"),
    普通话_香港("cmn-Hans-HK"),
    中文_台灣("cmn-Hant-TW"),
    粵語_香港("yue-Hant-HK"),
    日本語("ja-JP"),
    हिन्दी("hi-IN"),
    ภาษาไทย("th-TH");

    private final String lang;

    /**
     * When to declare language, for example {@code Lang.Canada.getLang()} to
     * avoid recreate wheels
     *
     * @return The language symbol.
     */
    public String getLang()
    {
        return lang;
    }

    private Lang(String lang)
    {
        this.lang = lang;
    }
}
