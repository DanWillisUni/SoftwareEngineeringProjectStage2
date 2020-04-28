package Model;

public class MealEaten {
    String foodName;
    String quantity;
    String calories;

    MealEaten(String foodName,String quantity, String calories){
        this.foodName=foodName;
        this.quantity = quantity;
        this.calories = calories;
    }

    public String getFoodName() {
        return foodName;
    }
    public String getQuantity() {
        return quantity;
    }
    public String getCalories() {
        return calories;
    }
}
