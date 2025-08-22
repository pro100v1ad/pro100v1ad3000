package main.java.com.engine3D.math;

/**
 * Класс для работы с трёхмерными векторами (x, y, z).
 */
public class Vector3f {
    // Координаты вектора
    public float x;
    public float y;
    public float z;

    // --- Конструкторы ---

    /**
     * Конструктор по умолчанию (вектор (0, 0, 0)).
     */
    public Vector3f() {
        this(0.0f, 0.0f, 0.0f);
    }

    /**
     * Конструктор с заданными координатами.
     * @param x Координата X.
     * @param y Координата Y.
     * @param z Координата Z.
     */
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Конструктор копирования.
     * @param other Другой вектор.
     */
    public Vector3f(Vector3f other) {
        this(other.x, other.y, other.z);
    }

    // --- Основные операции ---

    /**
     * Сложение с другим вектором.
     * @param other Другой вектор.
     * @return Новый вектор — результат сложения.
     */
    public Vector3f add(Vector3f other) {
        return new Vector3f(
                this.x + other.x,
                this.y + other.y,
                this.z + other.z
        );
    }

    /**
     * Сложение с числом (прибавление ко всем компонентам).
     * @param scalar Число.
     * @return Новый вектор.
     */
    public Vector3f add(float scalar) {
        return new Vector3f(
                this.x + scalar,
                this.y + scalar,
                this.z + scalar
        );
    }

    /**
     * Вычитание другого вектора.
     * @param other Другой вектор.
     * @return Новый вектор — результат вычитания.
     */
    public Vector3f subtract(Vector3f other) {
        return new Vector3f(
                this.x - other.x,
                this.y - other.y,
                this.z - other.z
        );
    }

    /**
     * Умножение на число (скаляр).
     * @param scalar Число.
     * @return Новый вектор.
     */
    public Vector3f multiply(float scalar) {
        return new Vector3f(
                this.x * scalar,
                this.y * scalar,
                this.z * scalar
        );
    }

    /**
     * Умножение на другой вектор (покомпонентное).
     * @param other Другой вектор.
     * @return Новый вектор.
     */
    public Vector3f multiply(Vector3f other) {
        return new Vector3f(
                this.x * other.x,
                this.y * other.y,
                this.z * other.z
        );
    }

    /**
     * Деление на число.
     * @param scalar Число.
     * @return Новый вектор.
     */
    public Vector3f divide(float scalar) {
        return new Vector3f(
                this.x / scalar,
                this.y / scalar,
                this.z / scalar
        );
    }

    /**
     * Скалярное произведение (dot product).
     * @param other Другой вектор.
     * @return Скалярное произведение.
     */
    public float dot(Vector3f other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    /**
     * Векторное произведение (cross product).
     * @param other Другой вектор.
     * @return Новый вектор — результат векторного произведения.
     */
    public Vector3f cross(Vector3f other) {
        return new Vector3f(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    /**
     * Длина вектора (модуль).
     * @return Длина вектора.
     */
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Нормализация вектора (приведение к единичной длине).
     * @return Нормализованный вектор.
     */
    public Vector3f normalize() {
        float len = length();
        if (len != 0) {
            return new Vector3f(
                    this.x / len,
                    this.y / len,
                    this.z / len
            );
        }
        return new Vector3f(this);
    }

    /**
     * Возвращает новый вектор с противоположными координатами.
     * @return Новый вектор (-x, -y, -z).
     */
    public Vector3f negate() {
        return new Vector3f(-x, -y, -z);
    }


    /**
     * Расстояние до другого вектора.
     * @param other Другой вектор.
     * @return Расстояние между векторами.
     */
    public float distanceTo(Vector3f other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        float dz = this.z - other.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Линейная интерполяция (LERP) между двумя векторами.
     * @param other Другой вектор.
     * @param alpha Коэффициент интерполяции (0.0 — this, 1.0 — other).
     * @return Новый вектор — результат интерполяции.
     */
    public Vector3f lerp(Vector3f other, float alpha) {
        return new Vector3f(
                this.x + alpha * (other.x - this.x),
                this.y + alpha * (other.y - this.y),
                this.z + alpha * (other.z - this.z)
        );
    }

    // --- Утилиты ---

    /**
     * Обнуление вектора.
     */
    public void zero() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    /**
     * Проверка на равенство с другим вектором.
     * @param other Другой вектор.
     * @return true, если векторы равны.
     */
    public boolean equals(Vector3f other) {
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    // --- Статические методы ---

    /**
     * Нулевой вектор.
     * @return Вектор (0, 0, 0).
     */
    public static Vector3f zero_() {
        return new Vector3f(0.0f, 0.0f, 0.0f);
    }

    /**
     * Вектор (1, 1, 1).
     * @return Вектор (1, 1, 1).
     */
    public static Vector3f one() {
        return new Vector3f(1.0f, 1.0f, 1.0f);
    }

    /**
     * Вектор вверх (0, 1, 0).
     * @return Вектор (0, 1, 0).
     */
    public static Vector3f up() {
        return new Vector3f(0.0f, 1.0f, 0.0f);
    }

    /**
     * Вектор вправо (1, 0, 0).
     * @return Вектор (1, 0, 0).
     */
    public static Vector3f right() {
        return new Vector3f(1.0f, 0.0f, 0.0f);
    }

    /**
     * Вектор вперёд (0, 0, 1).
     * @return Вектор (0, 0, 1).
     */
    public static Vector3f forward() {
        return new Vector3f(0.0f, 0.0f, 1.0f);
    }

    // --- Переопределение методов ---

    @Override
    public String toString() {
        return "Vector3f(" + x + ", " + y + ", " + z + ")";
    }
}
