package com.Tabetomo.shop;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Jacksonはデフォルトでenumを name() の文字列としてシリアライズしてしまい、
 * label/childrenなどの追加フィールドが無視されてしまう。
 * それを防ぎ、{"label": "...", "children": [...]} の形で出力させるための注釈。
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GenreCategory {
    JAPANESE("和食"),
    TEISHOKU("定食・食堂"),
    SUSHI("寿司", "回転寿司", "江戸前・高級寿司"),
    TEMPURA("天ぷら"),
    UNAGI("うなぎ"),
    SOBA_UDON("そば・うどん"),
    RAMEN("ラーメン", "家系", "二郎系", "博多豚骨", "煮干し系", "つけ麺", "まぜそば"),
    GYOZA("餃子"),
    DONBURI("丼もの"),
    OKONOMIYAKI("お好み焼き・もんじゃ"),
    YAKITORI("焼き鳥"),
    YAKINIKU("焼肉・ホルモン", "焼肉", "ホルモン", "ジンギスカン"),
    SHABU_SUKIYAKI("しゃぶしゃぶ・すき焼き"),
    NABE("鍋料理"),
    KAISEN("海鮮・魚介"),
    CURRY("カレー", "欧風カレー", "スープカレー", "インドカレー", "スパイスカレー"),

    WESTERN("洋食"),
    ITALIAN("イタリアン"),
    FRENCH("フレンチ"),
    STEAK_HAMBURG("ステーキ・ハンバーグ"),
    PASTA_PIZZA("パスタ・ピザ"),
    SPANISH_BAR("スペイン料理・バル"),

    CHINESE("中華", "町中華", "本格中華", "四川料理", "広東料理"),
    KOREAN("韓国料理", "サムギョプサル", "韓国焼肉", "参鶏湯"),
    THAI("タイ料理"),
    VIETNAMESE("ベトナム料理"),
    INDIAN("インド料理"),
    ASIAN_ETHNIC("アジアン・エスニック（その他）"),
    MEXICAN("メキシコ料理"),
    HALAL("ハラール"),
    WORLD_OTHER("各国料理（その他）"),

    IZAKAYA("居酒屋"),
    DINING_BAR("ダイニングバー"),
    BAR_COCKTAIL("バー・カクテル"),
    BEER_GARDEN("ビアガーデン・ビアホール"),
    WINE_BAR("ワインバー"),

    CAFE("カフェ・喫茶店"),
    SWEETS("スイーツ・デザート"),
    BAKERY_SANDWICH("パン・サンドイッチ"),

    FAST_FOOD("ファストフード"),
    BENTO("弁当・惣菜"),
    BUFFET("ビュッフェ・食べ放題"),
    DELIVERY_ONLY("デリバリー専門店"),

    OTHER("その他");

    private final String label;
    private final List<String> children;

    GenreCategory(String label, String... children) {
        this.label = label;
        this.children = List.of(children);
    }

    public String getLabel() {
        return label;
    }

    public List<String> getChildren() {
        return children;
    }

    /**
     * 子を持つ場合は "親（子）" 形式、持たない場合は親のラベルそのものを
     * 最終的なジャンル値として組み立てる。
     * 例: buildValue("家系") -> "ラーメン（家系）"
     */
    public String buildValue(String child) {
        if (children.isEmpty() || child == null || child.isBlank()) {
            return label;
        }
        return label + "（" + child + "）";
    }

    /**
     * 保存済みのジャンル文字列（例: "ラーメン（家系）"）が
     * どのGenreCategoryに属するかを判定する。編集画面の初期表示等で使用。
     */
    public static GenreCategory resolveCategory(String genreValue) {
        if (genreValue == null) return null;
        for (GenreCategory c : values()) {
            if (genreValue.equals(c.label) || genreValue.startsWith(c.label + "（")) {
                return c;
            }
        }
        return null;
    }

    public static boolean isValidValue(String genreValue) {
        if (genreValue == null) return false;
        for (GenreCategory c : values()) {
            if (genreValue.equals(c.label)) return true;
            for (String child : c.children) {
                if (genreValue.equals(c.buildValue(child))) return true;
            }
        }
        return false;
    }
}