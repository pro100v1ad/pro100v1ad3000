package main.java.com.engine3D.math;

/**
 * Класс для работы с матрицами 4×4.
 * Используется для трансформаций в 3D-графике.
 */
public class Matrix4f {
    // Элементы матрицы (хранятся в одном массиве для удобства)
    public float[] elements = new float[16];

    // --- Конструкторы ---

    /**
     * Конструктор по умолчанию (единичная матрица).
     */
    public Matrix4f() {
        setIdentity();
    }

    /**
     * Конструктор из массива.
     * @param elements Массив из 16 элементов.
     */
    public Matrix4f(float[] elements) {
        System.arraycopy(elements, 0, this.elements, 0, 16);
    }

    // --- Основные методы ---

    /**
     * Установить единичную матрицу.
     */
    public void setIdentity() {
        for (int i = 0; i < 16; i++) {
            elements[i] = 0.0f;
        }
        elements[0] = 1.0f;
        elements[5] = 1.0f;
        elements[10] = 1.0f;
        elements[15] = 1.0f;
    }

    /**
     * Умножение на другую матрицу.
     * @param other Другая матрица.
     * @return Новая матрица — результат умножения.
     */
    public Matrix4f multiply(Matrix4f other) {
        Matrix4f result = new Matrix4f();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                float sum = 0.0f;
                for (int i = 0; i < 4; i++) {
                    sum += get(row, i) * other.get(i, col);
                }
                result.set(row, col, sum);
            }
        }
        return result;
    }

    /**
     * Умножение на вектор.
     * @param vector Вектор.
     * @return Новый вектор — результат умножения.
     */
    public Vector3f multiply(Vector3f vector) {
        return multiply(vector, 1.0f);
    }

    /**
     * Умножение на вектор с учётом w-компоненты.
     * @param vector Вектор.
     * @param w Значение w-компоненты (обычно 1.0 для точек, 0.0 для направлений).
     * @return Новый вектор.
     */
    public Vector3f multiply(Vector3f vector, float w) {
        float x = vector.x * elements[0] + vector.y * elements[4] + vector.z * elements[8] + w * elements[12];
        float y = vector.x * elements[1] + vector.y * elements[5] + vector.z * elements[9] + w * elements[13];
        float z = vector.x * elements[2] + vector.y * elements[6] + vector.z * elements[10] + w * elements[14];
        return new Vector3f(x, y, z);
    }

    /**
     * Транспонирование матрицы.
     * @return Новая матрица — транспонированная.
     */
    public Matrix4f transpose() {
        Matrix4f result = new Matrix4f();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                result.set(row, col, get(col, row));
            }
        }
        return result;
    }

    /**
     * Вычисление обратной матрицы.
     * @return Новая матрица — обратная.
     * @throws Exception Если матрица вырожденная (необратимая).
     */
    public Matrix4f invert() throws Exception {
        float[] inv = new float[16];
        float det = determinant();
        if (Math.abs(det) < 1e-6f) {
            throw new Exception("Matrix is not invertible (determinant is zero).");
        }

        inv[0] = getMinor(5, 10, 15, 6, 11, 14, 7, 11, 13) / det;
        inv[1] = -getMinor(4, 10, 15, 6, 11, 14, 7, 10, 13) / det;
        inv[2] = getMinor(4, 9, 15, 6, 11, 13, 7, 9, 14) / det;
        inv[3] = -getMinor(4, 9, 14, 6, 10, 13, 7, 9, 11) / det;

        inv[4] = -getMinor(1, 10, 15, 2, 11, 14, 3, 11, 13) / det;
        inv[5] = getMinor(0, 10, 15, 2, 11, 14, 3, 10, 13) / det;
        inv[6] = -getMinor(0, 9, 15, 2, 11, 13, 3, 9, 14) / det;
        inv[7] = getMinor(0, 9, 14, 2, 10, 13, 3, 9, 11) / det;

        inv[8] = getMinor(1, 6, 15, 2, 7, 14, 3, 7, 13) / det;
        inv[9] = -getMinor(0, 6, 15, 2, 7, 14, 3, 6, 13) / det;
        inv[10] = getMinor(0, 6, 14, 2, 7, 13, 3, 6, 11) / det;
        inv[11] = -getMinor(0, 6, 13, 2, 7, 11, 3, 6, 7) / det;

        inv[12] = -getMinor(1, 6, 10, 2, 7, 9, 3, 7, 8) / det;
        inv[13] = getMinor(0, 6, 10, 2, 7, 9, 3, 6, 8) / det;
        inv[14] = -getMinor(0, 6, 9, 2, 7, 8, 3, 6, 7) / det;
        inv[15] = getMinor(0, 6, 8, 2, 7, 8, 3, 6, 7) / det;

        return new Matrix4f(inv);
    }

    /**
     * Вычисление минора для обратной матрицы.
     */
    private float getMinor(int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        return elements[i1] * (elements[i5] * elements[i9] - elements[i6] * elements[i8]) -
                elements[i2] * (elements[i4] * elements[i9] - elements[i6] * elements[i7]) +
                elements[i3] * (elements[i4] * elements[i8] - elements[i5] * elements[i7]);
    }

    /**
     * Вычисление определителя матрицы.
     * @return Определитель.
     */
    public float determinant() {
        return elements[0] * getMinor(5, 10, 15, 6, 11, 14, 7, 11, 13) -
                elements[4] * getMinor(1, 10, 15, 2, 11, 14, 3, 11, 13) +
                elements[8] * getMinor(1, 6, 15, 2, 7, 14, 3, 7, 13) -
                elements[12] * getMinor(1, 6, 10, 2, 7, 9, 3, 7, 8);
    }

    /**
     * Получение элемента матрицы.
     * @param row Строка (0-3).
     * @param col Столбец (0-3).
     * @return Значение элемента.
     */
    public float get(int row, int col) {
        return elements[row * 4 + col];
    }

    /**
     * Установка элемента матрицы.
     * @param row Строка (0-3).
     * @param col Столбец (0-3).
     * @param value Значение.
     */
    public void set(int row, int col, float value) {
        elements[row * 4 + col] = value;
    }

    // --- Статические методы для создания матриц ---

    /**
     * Матрица перемещения (translation).
     * @param position Вектор перемещения.
     * @return Матрица перемещения.
     */
    public static Matrix4f translation(Vector3f position) {
        Matrix4f result = new Matrix4f();
        result.set(0, 3, position.x);
        result.set(1, 3, position.y);
        result.set(2, 3, position.z);
        return result;
    }

    /**
     * Матрица вращения вокруг оси X.
     * @param angle Угол в радианах.
     * @return Матрица вращения.
     */
    public static Matrix4f rotationX(float angle) {
        Matrix4f result = new Matrix4f();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        result.elements[5] = cos;
        result.elements[6] = -sin;
        result.elements[9] = sin;
        result.elements[10] = cos;
        return result;
    }

    /**
     * Матрица вращения вокруг оси Y.
     * @param angle Угол в радианах.
     * @return Матрица вращения.
     */
    public static Matrix4f rotationY(float angle) {
        Matrix4f result = new Matrix4f();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        result.elements[0] = cos;
        result.elements[2] = sin;
        result.elements[8] = -sin;
        result.elements[10] = cos;
        return result;
    }

    /**
     * Матрица вращения вокруг оси Z.
     * @param angle Угол в радианах.
     * @return Матрица вращения.
     */
    public static Matrix4f rotationZ(float angle) {
        Matrix4f result = new Matrix4f();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        result.elements[0] = cos;
        result.elements[1] = -sin;
        result.elements[4] = sin;
        result.elements[5] = cos;
        return result;
    }

    /**
     * Матрица масштабирования.
     * @param scale Вектор масштаба.
     * @return Матрица масштабирования.
     */
    public static Matrix4f scaling(Vector3f scale) {
        Matrix4f result = new Matrix4f();
        result.elements[0] = scale.x;
        result.elements[5] = scale.y;
        result.elements[10] = scale.z;
        return result;
    }

    /**
     * Матрица перспективной проекции.
     * @param fov Угол обзора (в радианах).
     * @param aspect Соотношение сторон (width / height).
     * @param near Ближняя плоскость отсечения.
     * @param far Дальняя плоскость отсечения.
     * @return Матрица проекции.
     */
    public static Matrix4f perspective(float fov, float aspect, float near, float far) {
        Matrix4f result = new Matrix4f();
        float tanHalfFov = (float) Math.tan(fov / 2.0f);
        result.elements[0] = 1.0f / (aspect * tanHalfFov);
        result.elements[5] = 1.0f / tanHalfFov;
        result.elements[10] = -(far + near) / (far - near);
        result.elements[11] = -1.0f;
        result.elements[14] = -(2.0f * far * near) / (far - near);
        result.elements[15] = 0.0f;
        return result;
    }

    /**
     * Матрица ортографической проекции.
     * @param left Левая граница.
     * @param right Правая граница.
     * @param bottom Нижняя граница.
     * @param top Верхняя граница.
     * @param near Ближняя плоскость отсечения.
     * @param far Дальняя плоскость отсечения.
     * @return Матрица проекции.
     */
    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f result = new Matrix4f();
        result.elements[0] = 2.0f / (right - left);
        result.elements[5] = 2.0f / (top - bottom);
        result.elements[10] = -2.0f / (far - near);
        result.elements[12] = -(right + left) / (right - left);
        result.elements[13] = -(top + bottom) / (top - bottom);
        result.elements[14] = -(far + near) / (far - near);
        return result;
    }

    /**
     * Матрица вида (view matrix).
     * @param position Позиция камеры.
     * @param rotation Вращение камеры (в радианах).
     * @return Матрица вида.
     */
    public static Matrix4f view(Vector3f position, Vector3f rotation) {
        Matrix4f result = new Matrix4f();
        Matrix4f rotationMatrix = rotationX(rotation.x).multiply(rotationY(rotation.y)).multiply(rotationZ(rotation.z));
        Matrix4f translationMatrix = translation(position.negate());
        return rotationMatrix.multiply(translationMatrix);
    }

    // --- Переопределение методов ---

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                sb.append(String.format("%.2f", get(row, col))).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
