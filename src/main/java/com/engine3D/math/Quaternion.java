package main.java.com.engine3D.math;

/**
 * Класс для работы с кватернионами.
 * Используется для представления вращений в 3D-пространстве.
 */
public class Quaternion {
    // Компоненты кватерниона: x, y, z — векторная часть, w — скалярная часть
    public float x;
    public float y;
    public float z;
    public float w;

    // --- Конструкторы ---

    /**
     * Конструктор по умолчанию (единичный кватернион, нет вращения).
     */
    public Quaternion() {
        this(0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Конструктор с заданными компонентами.
     * @param x Векторная часть (x).
     * @param y Векторная часть (y).
     * @param z Векторная часть (z).
     * @param w Скалярная часть.
     */
    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Конструктор из углов Эйлера (в радианах).
     * @param pitch Угол наклона (X).
     * @param yaw Угол рыскания (Y).
     * @param roll Угол крена (Z).
     */
    public Quaternion(float pitch, float yaw, float roll) {
        float halfPitch = pitch * 0.5f;
        float halfYaw = yaw * 0.5f;
        float halfRoll = roll * 0.5f;

        float sinPitch = (float) Math.sin(halfPitch);
        float cosPitch = (float) Math.cos(halfPitch);
        float sinYaw = (float) Math.sin(halfYaw);
        float cosYaw = (float) Math.cos(halfYaw);
        float sinRoll = (float) Math.sin(halfRoll);
        float cosRoll = (float) Math.cos(halfRoll);

        this.x = sinRoll * cosPitch * cosYaw - cosRoll * sinPitch * sinYaw;
        this.y = cosRoll * sinPitch * cosYaw + sinRoll * cosPitch * sinYaw;
        this.z = cosRoll * cosPitch * sinYaw - sinRoll * sinPitch * cosYaw;
        this.w = cosRoll * cosPitch * cosYaw + sinRoll * sinPitch * sinYaw;
    }

    /**
     * Конструктор из оси и угла вращения (в радианах).
     * @param axis Ось вращения (нормализованный вектор).
     * @param angle Угол вращения.
     */
    public Quaternion(Vector3f axis, float angle) {
        float halfAngle = angle * 0.5f;
        float sin = (float) Math.sin(halfAngle);
        this.x = axis.x * sin;
        this.y = axis.y * sin;
        this.z = axis.z * sin;
        this.w = (float) Math.cos(halfAngle);
    }

    // --- Основные операции ---

    /**
     * Нормализация кватерниона.
     * @return Нормализованный кватернион.
     */
    public Quaternion normalize() {
        float length = (float) Math.sqrt(x * x + y * y + z * z + w * w);
        if (length != 0.0f) {
            float invLength = 1.0f / length;
            return new Quaternion(
                    x * invLength,
                    y * invLength,
                    z * invLength,
                    w * invLength
            );
        }
        return this;
    }

    /**
     * Умножение кватернионов (композиция вращений).
     * @param other Другой кватернион.
     * @return Новый кватернион — результат умножения.
     */
    public Quaternion multiply(Quaternion other) {
        float newX = this.w * other.x + this.x * other.w + this.y * other.z - this.z * other.y;
        float newY = this.w * other.y - this.x * other.z + this.y * other.w + this.z * other.x;
        float newZ = this.w * other.z + this.x * other.y - this.y * other.x + this.z * other.w;
        float newW = this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z;
        return new Quaternion(newX, newY, newZ, newW);
    }

    /**
     * Сопряжённый кватернион.
     * @return Сопряжённый кватернион.
     */
    public Quaternion conjugate() {
        return new Quaternion(-x, -y, -z, w);
    }

    /**
     * Обратный кватернион.
     * @return Обратный кватернион.
     */
    public Quaternion inverse() {
        float lengthSq = x * x + y * y + z * z + w * w;
        if (lengthSq != 0.0f) {
            float invLengthSq = 1.0f / lengthSq;
            return new Quaternion(
                    -x * invLengthSq,
                    -y * invLengthSq,
                    -z * invLengthSq,
                    w * invLengthSq
            );
        }
        return this;
    }

    /**
     * Вращение вектора с помощью кватерниона.
     * @param vector Вектор для вращения.
     * @return Новый вектор — результат вращения.
     */
    public Vector3f rotate(Vector3f vector) {
        Quaternion q = new Quaternion(vector.x, vector.y, vector.z, 0.0f);
        Quaternion result = this.multiply(q).multiply(this.conjugate());
        return new Vector3f(result.x, result.y, result.z);
    }

    /**
     * Преобразование кватерниона в матрицу вращения 4x4.
     * @return Матрица вращения.
     */
    public Matrix4f toRotationMatrix() {
        Matrix4f matrix = new Matrix4f();

        float xx = x * x;
        float yy = y * y;
        float zz = z * z;
        float xy = x * y;
        float xz = x * z;
        float yz = y * z;
        float wx = w * x;
        float wy = w * y;
        float wz = w * z;

        matrix.set(0, 0, 1.0f - 2.0f * (yy + zz));
        matrix.set(0, 1, 2.0f * (xy - wz));
        matrix.set(0, 2, 2.0f * (xz + wy));

        matrix.set(1, 0, 2.0f * (xy + wz));
        matrix.set(1, 1, 1.0f - 2.0f * (xx + zz));
        matrix.set(1, 2, 2.0f * (yz - wx));

        matrix.set(2, 0, 2.0f * (xz - wy));
        matrix.set(2, 1, 2.0f * (yz + wx));
        matrix.set(2, 2, 1.0f - 2.0f * (xx + yy));

        matrix.set(3, 3, 1.0f);

        return matrix;
    }

    /**
     * Преобразование кватерниона в углы Эйлера (в радианах).
     * @return Массив из трёх углов: [pitch, yaw, roll].
     */
    public float[] toEulerAngles() {
        float[] angles = new float[3];

        // Pitch (X)
        float sinPitch = 2.0f * (w * x + y * z);
        float cosPitch = 1.0f - 2.0f * (x * x + y * y);
        angles[0] = (float) Math.atan2(sinPitch, cosPitch);

        // Yaw (Y)
        float sinYaw = 2.0f * (w * y + z * x);
        float cosYaw = 1.0f - 2.0f * (y * y + z * z);
        angles[1] = (float) Math.atan2(sinYaw, cosYaw);

        // Roll (Z)
        float sinRoll = 2.0f * (w * z + x * y);
        float cosRoll = 1.0f - 2.0f * (z * z + x * x);
        angles[2] = (float) Math.atan2(sinRoll, cosRoll);

        return angles;
    }

    /**
     * Линейная интерполяция (LERP) между двумя кватернионами.
     * @param other Другой кватернион.
     * @param alpha Коэффициент интерполяции (0.0 — this, 1.0 — other).
     * @return Новый кватернион — результат интерполяции.
     */
    public Quaternion lerp(Quaternion other, float alpha) {
        float x = this.x + alpha * (other.x - this.x);
        float y = this.y + alpha * (other.y - this.y);
        float z = this.z + alpha * (other.z - this.z);
        float w = this.w + alpha * (other.w - this.w);
        return new Quaternion(x, y, z, w).normalize();
    }

    /**
     * Сферическая интерполяция (SLERP) между двумя кватернионами.
     * @param other Другой кватернион.
     * @param alpha Коэффициент интерполяции (0.0 — this, 1.0 — other).
     * @return Новый кватернион — результат интерполяции.
     */
    public Quaternion slerp(Quaternion other, float alpha) {
        float dot = this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;

        // Если dot < 0, используем -other для кратчайшего пути
        if (dot < 0.0f) {
            other = new Quaternion(-other.x, -other.y, -other.z, -other.w);
            dot = -dot;
        }

        // Линейная интерполяция, если кватернионы почти совпадают
        if (dot > 0.9995f) {
            return lerp(other, alpha);
        }

        // Вычисляем угол между кватернионами
        float theta = (float) Math.acos(dot);
        float sinTheta = (float) Math.sin(theta);

        float a = (float) Math.sin((1.0f - alpha) * theta) / sinTheta;
        float b = (float) Math.sin(alpha * theta) / sinTheta;

        float x = a * this.x + b * other.x;
        float y = a * this.y + b * other.y;
        float z = a * this.z + b * other.z;
        float w = a * this.w + b * other.w;

        return new Quaternion(x, y, z, w);
    }

    // --- Переопределение методов ---

    @Override
    public String toString() {
        return "Quaternion(" + x + ", " + y + ", " + z + ", " + w + ")";
    }
}
