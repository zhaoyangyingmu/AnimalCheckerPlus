package entity.animal.instance;

import entity.animal.type.AnimalWithoutSkill;

/**
 * Created by 谢东方xdf on 2017/1/3.
 */
public class Dog extends AnimalWithoutSkill {

    @Override
    protected String getName() {
        return "狗";
    }
}
