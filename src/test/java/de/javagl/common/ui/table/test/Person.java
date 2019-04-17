package de.javagl.common.ui.table.test;

/**
 * Dummy class for this test package
 */
@SuppressWarnings("javadoc")
public class Person
{
    private String name;
    private int age;
    private float height;

    public Person(String name, int age, float height)
    {
        this.name = name;
        this.age = age;
        this.height = height;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        System.out.println("Name changed to " + name);
        this.name = name;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        System.out.println("Age changed to " + age);
        this.age = age;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        System.out.println("Height changed to " + height);
        this.height = height;
    }

}
