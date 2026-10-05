package github.goldblock.goety_aether.utils;

@FunctionalInterface
public interface Easing {
    float calculate(float f);

    default float interpolate(float f, float from, float to) {
        return from + calculate(f) * (to - from);
    }

    Easing OUT_BACK = x -> {
        float c1 = 1.70158f;
        float c3 = c1 + 1;
        return (float) (1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2));
    };

    Easing IN_QUART = x -> x * x * x * x;

    Easing OUT_ELASTIC = x -> {
        float c4 = (float) ((2 * Math.PI) / 3);
        return x == 0 ? 0 : (float) (x == 1 ? 1 : Math.pow(2, -10 * x) * Math.sin((x * 10 - 0.75) * c4) + 1);
    };
}
