/* Código de tres direcciones: variables globales y referencias a memoria. */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
double stack[100000];
double heap[100000];
/* P identifica la base de pila; H señala la siguiente celda libre del heap. */
double P = 0;
double H = 0;
void print_string(int index) {
    while (heap[index] != 0 && heap[index] != -1) putchar((char)heap[index++]);
}
double concat_cadenas(double left, double right) {
    int start = (int)H;
    for (int i = (int)left; heap[i] != 0 && heap[i] != -1; i++) heap[(int)H++] = heap[i];
    for (int i = (int)right; heap[i] != 0 && heap[i] != -1; i++) heap[(int)H++] = heap[i];
    heap[(int)H++] = 0;
    return start;
}
double number_to_text(double value) {
    char buffer[64];
    snprintf(buffer, sizeof(buffer), "%.15g", value);
    int start = (int)H;
    for (int i = 0; buffer[i]; i++) heap[(int)H++] = buffer[i];
    heap[(int)H++] = 0;
    return start;
}
double read_text(void) {
    char buffer[4096];
    int start = (int)H;
    if (scanf(" %4095[^\n]", buffer) != 1) buffer[0] = 0;
    for (int i = 0; buffer[i]; i++) heap[(int)H++] = buffer[i];
    heap[(int)H++] = 0;
    return start;
}
double char_to_text(double value) {
    int start = (int)H;
    heap[(int)H++] = value;
    heap[(int)H++] = 0;
    return start;
}
double read_character(void) {
    char value = 0;
    scanf(" %c", &value);
    return (unsigned char)value;
}

int main(void) {
    double t0 = 0;
    double t1 = 0;
    double t2 = 0;
    double t3 = 0;
    t0 = H;
    heap[(int)H++] = 72;
    heap[(int)H++] = 111;
    heap[(int)H++] = 108;
    heap[(int)H++] = 97;
    heap[(int)H++] = 44;
    heap[(int)H++] = 32;
    heap[(int)H++] = 109;
    heap[(int)H++] = 117;
    heap[(int)H++] = 110;
    heap[(int)H++] = 100;
    heap[(int)H++] = 111;
    heap[(int)H++] = 0;
    stack[0] = t0;
    stack[1] = 2026;
    stack[2] = 0;
    t1 = stack[1] + 2026;
    t2 = t1 - 2026;
    stack[2] = t2;
    print_string((int)stack[0]);
    printf("\n");
    t3 = H;
    heap[(int)H++] = 69;
    heap[(int)H++] = 108;
    heap[(int)H++] = 32;
    heap[(int)H++] = 116;
    heap[(int)H++] = 111;
    heap[(int)H++] = 116;
    heap[(int)H++] = 97;
    heap[(int)H++] = 108;
    heap[(int)H++] = 32;
    heap[(int)H++] = 101;
    heap[(int)H++] = 115;
    heap[(int)H++] = 0;
    print_string((int)t3);
    printf("\n");
    printf("%d", (int)stack[2]);
    printf("\n");
    return 0;
}
