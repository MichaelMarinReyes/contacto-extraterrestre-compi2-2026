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
    double t4 = 0;
    double t5 = 0;
    double t6 = 0;
    stack[0] = 22;
    stack[1] = 0;
    stack[2] = 0;
    stack[3] = 0;
    if (stack[0] >= 18) goto et2;
    goto et3;
et2:;
    t0 = H;
    heap[(int)H++] = 69;
    heap[(int)H++] = 115;
    heap[(int)H++] = 32;
    heap[(int)H++] = 109;
    heap[(int)H++] = 97;
    heap[(int)H++] = 121;
    heap[(int)H++] = 111;
    heap[(int)H++] = 114;
    heap[(int)H++] = 32;
    heap[(int)H++] = 100;
    heap[(int)H++] = 101;
    heap[(int)H++] = 32;
    heap[(int)H++] = 101;
    heap[(int)H++] = 100;
    heap[(int)H++] = 97;
    heap[(int)H++] = 100;
    heap[(int)H++] = 0;
    print_string((int)t0);
    printf("\n");
    goto et1;
et3:;
    if (stack[0] < 18) goto et4;
    goto et5;
et4:;
    t1 = H;
    heap[(int)H++] = 69;
    heap[(int)H++] = 115;
    heap[(int)H++] = 32;
    heap[(int)H++] = 109;
    heap[(int)H++] = 101;
    heap[(int)H++] = 110;
    heap[(int)H++] = 111;
    heap[(int)H++] = 114;
    heap[(int)H++] = 32;
    heap[(int)H++] = 100;
    heap[(int)H++] = 101;
    heap[(int)H++] = 32;
    heap[(int)H++] = 101;
    heap[(int)H++] = 100;
    heap[(int)H++] = 97;
    heap[(int)H++] = 100;
    heap[(int)H++] = 0;
    print_string((int)t1);
    printf("\n");
    goto et1;
et5:;
et1:;
et6:;
    if (stack[2] < 5) goto et7;
    goto et8;
et7:;
    t2 = stack[1] + stack[2];
    stack[1] = t2;
    t3 = stack[2] + 1;
    stack[2] = t3;
    goto et6;
et8:;
et9:;
    t4 = stack[1] + 100;
    stack[1] = t4;
et10:;
    if (stack[1] > 200) goto et9;
    goto et11;
et11:;
    stack[3] = 0;
et12:;
    if (stack[3] < 10) goto et13;
    goto et15;
et13:;
    if (stack[3] == 3) goto et17;
    goto et18;
et17:;
    goto et14;
    goto et16;
et18:;
et16:;
    if (stack[3] == 7) goto et20;
    goto et21;
et20:;
    goto et15;
    goto et19;
et21:;
et19:;
    printf("%d", (int)stack[3]);
    printf("\n");
et14:;
    t5 = stack[3] + 1;
    stack[3] = t5;
    goto et12;
et15:;
    t6 = H;
    heap[(int)H++] = 84;
    heap[(int)H++] = 111;
    heap[(int)H++] = 116;
    heap[(int)H++] = 97;
    heap[(int)H++] = 108;
    heap[(int)H++] = 0;
    print_string((int)t6);
    printf("\n");
    printf("%d", (int)stack[1]);
    printf("\n");
    return 0;
}
