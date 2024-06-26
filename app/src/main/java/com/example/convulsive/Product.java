package com.example.convulsive;

import java.util.ArrayList;
import java.util.List;

public class Product{
    private int[] images;
    private String[] sizes;
    private String description;
    private String name;
    private int price;
    private int imageResource;

    public Product(String name, int price, int imageResource, int[] images, String[] sizes, String description) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.sizes = sizes;
        this.description = description;
        this.images = images;
    }
    public int[] getImages() {return images;}

    public String getDescription() {
        return description;
    }
    public String[] getSizes() {return sizes;}
    public String getName() {return name;}
    public int getPrice() {return price;}
    public int getImageResource() {return imageResource;}

    public static List<Product> generateProductList(){
        ArrayList<Product> products = new ArrayList<>();
        Product fxckworkinghoodie = new Product("FXCK WORKING HOODIE", 6000, R.drawable.c0_1, new int[]{R.drawable.c0_1, R.drawable.c0_2, R.drawable.c0_3}, new String[]{"M", "L", "XL"}, "- Оверсайз крой\n" +
                "- Плотность ткани 400гр/м (80% хлопок/20% полиэстер)\n" +
                "- Вышивка на капюшоне, рукаве и над карманом спереди\n" +
                "- Шелкография на спине\n" +
                "- Тираж ограничен\n(М на рост до 175 см / L на рост 175-185 / XL на рост 185+) (6000р)");
        Product devilscarfv2 = new Product("DEVIL SCARF V2 LIMITED", 2500, R.drawable.c1_1, new int[]{R.drawable.c1_1, R.drawable.c1_2}, new String[]{}, "- Размер 200x25cm\n" +
                "- Состав 60% шерсть / 40% акрил\n" +
                "- Бахрома на конце шарфа\n" +
                "- Двухсторонний\n" +
                "- Без рестока!!! Тираж ограничен");
        Product graveyardhopeshoodie = new Product("GRAVEYARD HOPES PLUSH HOODIE", 6500, R.drawable.c2_1, new int[]{R.drawable.c2_1, R.drawable.c2_2, R.drawable.c2_3}, new String[]{"M-L", "XL"}, "- Оверсайз крой\n" +
                "- ВСЕ ДЕТАЛИ ВЫШИВКА\n" +
                "- Материал: барашек\n" +
                "- Состав: 60 хлопок / 40 полиэстер\n" +
                "- Ткань премиум качества, очень приятная и тёплая");
        Product memoriestee = new Product("MEMORIES TEE LIMITED", 2500, R.drawable.c3_1, new int[]{R.drawable.c3_1, R.drawable.c3_2, R.drawable.c3_3, R.drawable.c3_4}, new String[]{"M", "L", "XL"}, "- Оверсайз крой\n" +
                "- Плотность ткани 210гр/м, 95% хлопок/5% лайкра\n" +
                "- Вышивка на рукаве\n" +
                "- Шелкография на спине и груди\n" +
                "- Без рестока!!! Тираж ограничен\n" +
                "(М на рост до 175 см / L на рост 175-185 / XL на рост 185+)");
        Product hautegotika = new Product("HAUTE GOTIKA ZIP HOODIE", 6300, R.drawable.c4_1, new int[]{R.drawable.c4_1, R.drawable.c4_2, R.drawable.c4_3, R.drawable.c4_4}, new String[]{"M", "L", "XL"}, "- Оверсайз крой\n" +
                "- Плотность ткани 400гр/м (80% хлопок/20% полиэстер)\n" +
                "- Все детали вышивка\n" +
                "- Качественная металлическая молния\n" +
                "- Тираж ограничен");
        Product hatetshirt = new Product("THE HATE T-SHIRT", 2600, R.drawable.c5_1, new int[]{R.drawable.c5_1, R.drawable.c5_2, R.drawable.c5_3, R.drawable.c5_4}, new String[]{"M-L", "XL"}, "расцветка в двух цветах (чёрная/белая)\n" +
                "оверсайз крой\n" +
                "ткань 95 хлопок / 5 лайкра\n" +
                "нанесение принта шелкография\n" +
                "размеры:\n" +
                "baby (m-l) на рост до 180\n" +
                "big (xl+) на рост 180-200");
        Product devilscarf = new Product("DEVIL SCARF LIMITED", 2500, R.drawable.c6_1, new int[]{R.drawable.c6_1, R.drawable.c6_2, R.drawable.c6_3}, new String[]{}, "- Размер 200x25cm\n" +
                "- Состав 60% шерсть / 40% акрил\n" +
                "- Двухсторонний\n" +
                "- Без рестока!!! Тираж ограничен");
        Product phantomtroupe = new Product("PHANTOM TROUPE LONGSLEEVE", 5500, R.drawable.c7_1, new int[]{R.drawable.c7_1, R.drawable.c7_2, R.drawable.c7_3, R.drawable.c7_4}, new String[]{"M-L", "XL"}, "''phantom troupe''\n" +
                "оверсайз крой\n" +
                "ткань: велюр\n" +
                "все детали машинная вышивка\n" +
                "baby (m-l) на рост до 180\n" +
                "big (xl+) на рост 180-200");
        products.add(fxckworkinghoodie);products.add(devilscarfv2);products.add(graveyardhopeshoodie);products.add(memoriestee);products.add(hautegotika);
        products.add(hatetshirt);products.add(devilscarf);products.add(phantomtroupe);
        return products;
    }

}