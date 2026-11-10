package com.Tabetomo.shop;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * GenreCategoryをさらに大きな括りでグルーピングするための最上位カテゴリー。
 * ジャンル選択UI（パンくず）の一番最初の階層として使う。
 * ※ 実際にDBへ保存される値はGenreCategory側のbuildValue()で決まるので、
 *    このグループ自体は保存値には影響しない（あくまで選ぶ時の導線）。
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GenreGroup {

    JAPANESE_STYLE("和食系", List.of(
            GenreCategory.JAPANESE, GenreCategory.TEISHOKU, GenreCategory.SUSHI, GenreCategory.TEMPURA,
            GenreCategory.UNAGI, GenreCategory.SOBA_UDON, GenreCategory.RAMEN, GenreCategory.GYOZA,
            GenreCategory.DONBURI, GenreCategory.OKONOMIYAKI, GenreCategory.YAKITORI, GenreCategory.YAKINIKU,
            GenreCategory.SHABU_SUKIYAKI, GenreCategory.NABE, GenreCategory.KAISEN, GenreCategory.CURRY
    )),

    WESTERN_STYLE("洋食系", List.of(
            GenreCategory.WESTERN, GenreCategory.ITALIAN, GenreCategory.FRENCH,
            GenreCategory.STEAK_HAMBURG, GenreCategory.PASTA_PIZZA, GenreCategory.SPANISH_BAR
    )),

    ASIAN_STYLE("中華・エスニック系", List.of(
            GenreCategory.CHINESE, GenreCategory.KOREAN, GenreCategory.THAI, GenreCategory.VIETNAMESE,
            GenreCategory.INDIAN, GenreCategory.ASIAN_ETHNIC, GenreCategory.MEXICAN,
            GenreCategory.HALAL, GenreCategory.WORLD_OTHER
    )),

    BAR_STYLE("お酒・バー系", List.of(
            GenreCategory.IZAKAYA, GenreCategory.DINING_BAR, GenreCategory.BAR_COCKTAIL,
            GenreCategory.BEER_GARDEN, GenreCategory.WINE_BAR
    )),

    CAFE_STYLE("カフェ・軽食系", List.of(
            GenreCategory.CAFE, GenreCategory.SWEETS, GenreCategory.BAKERY_SANDWICH,
            GenreCategory.FAST_FOOD, GenreCategory.BENTO, GenreCategory.BUFFET, GenreCategory.DELIVERY_ONLY
    )),

    OTHER_STYLE("その他", List.of(
            GenreCategory.OTHER
    ));

    private final String label;
    private final List<GenreCategory> categories;

    GenreGroup(String label, List<GenreCategory> categories) {
        this.label = label;
        this.categories = categories;
    }

    public String getLabel() {
        return label;
    }

    public List<GenreCategory> getCategories() {
        return categories;
    }

    /**
     * 保存済みのジャンル文字列がどのGenreGroupに属するかを判定する。
     * 編集画面などでの初期表示に使用。
     */
    public static GenreGroup resolveGroup(String genreValue) {
        GenreCategory category = GenreCategory.resolveCategory(genreValue);
        if (category == null) return null;
        for (GenreGroup g : values()) {
            if (g.categories.contains(category)) return g;
        }
        return null;
    }
}