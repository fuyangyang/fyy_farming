package basic.switch_enum;

/**
 * enum提供values()方法遍历所有枚举值
 */
enum Fruit
{
    APPLE, BANANA, ORANGE, WATERMELON
}

public class EnumTest
{
    public static void main(String[] args)
    {
        for(Fruit fruit : Fruit.values())
        {
            test(fruit);
        }
    }

    public static void test(Fruit fruit)
    {
        switch (fruit)
        {
            case APPLE:
                System.out.println("This is an apple");
                break;
            case BANANA:
                System.out.println("This is a banana");
                break;
            case ORANGE:
                System.out.println("This is an orange");
                break;
            case WATERMELON:
                System.out.println("This is a watermelon");
                break;

            default:
                break;
        }
    }
}
