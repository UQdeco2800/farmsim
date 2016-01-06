     * Set the food value of the consumable. As an Animal consumes the
     * consumable this food value will be reduced until it is 0. If a consumable
     * is not designed to be edible, set this value to 0 in the constructor.
     * 
     * @param newFoodValue 0 <= newFoodValue <= 100
     * Set the drink(water) value of the consumable. As an Animal consumes the
     * consumable this drink value will be reduced until it is 0. If a
     * consumable is not designed to be consumable, set this value to 0 in the
     * constructor. NOTE: While the concept of "drinking" from a plant may seem
     * weird, think of it as the moisture content from the plant being absorbed
     * by the animal.
     * 
     * @param newDrinkValue 0 <= newDrinkValue <= 100
     * Adds the consumable to the ConsumableManager. This method should simply
     * call AnimalConsumableManager.getInstace().add(this)
     * The ConsumableManager will call this method when the consumable has no
     * food or drink value left. It can either be ignored or, in the case of
     * crops and such, used to handle a crop being fully consumed.
