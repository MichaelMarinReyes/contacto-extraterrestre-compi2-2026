# Ensamblador RISC-V (RV32IMF/D) generado por Contacto 3xtrat3rr3str3D
# Compatible con RARS: el numero de servicio va en a7 y los argumentos en a0.
# Pila: stack (frames por invocacion). Monticulo: heap. Marcas: otype.
.data
.align 3
stack: .space 800000
heap:  .space 800000
otype: .space 800000
H:     .double 0.0
P:     .double 0.0
RET:   .double 0.0
RA:    .double 0.0
_txt:  .space 4096
.rodata
.align 3
cst0: .double 0.0
cst1: .double 3.0
cst2: .double 1.0
cst3: .double 2.0
cst4: .double 178.0
cst5: .double 7.0
cst6: .double 4.0
cst7: .double -1.0
cst8: .double 6.0
cst9: .double 10.0
cst10: .double 8.0
cst11: .double 9.0
cst12: .double 5.0
cst13: .double 11.0
cst14: .double 12.0
cst15: .double 13.0
cst16: .double 14.0
cst17: .double 15.0
cst18: .double 16.0
cst19: .double 17.0
cst20: .double 18.0
cst21: .double 19.0
cst22: .double 20.0
cst23: .double 21.0
cst24: .double 22.0
cst25: .double 23.0
cst26: .double 24.0
cst27: .double 25.0
cst28: .double 26.0
cst29: .double 27.0
cst30: .double 28.0
cst31: .double 29.0
cst32: .double 30.0
cst33: .double 31.0
cst34: .double 32.0
cst35: .double 33.0
cst36: .double 34.0
cst37: .double 35.0
cst38: .double 36.0
cst39: .double 37.0
cst40: .double 38.0
cst41: .double 39.0
cst42: .double 40.0
cst43: .double 41.0
cst44: .double 42.0
cst45: .double 43.0
cst46: .double 44.0
cst47: .double 45.0
cst48: .double 46.0
cst49: .double 47.0
cst50: .double 48.0
cst51: .double 49.0
cst52: .double 50.0
cst53: .double 51.0
cst54: .double 52.0
cst55: .double 53.0
.text
.globl main
main:
    li t0, 8
    fcvt.d.w ft0, t0
    la t0, P
    fsd ft0, 0(t0)
    la t0, cst0
    fld fa0, 0(t0)
    la t0, stack
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 8
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 16
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 24
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 32
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, H
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst1
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, H
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, otype
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst2
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_ListaDoble_a0
L_ret_1:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, H
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst1
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, H
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, otype
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst5
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst3
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_ListaDoble_a1
L_ret_2:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 48
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, H
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst6
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, H
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst1
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst1
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 56
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 76
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 73
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 83
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 84
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 65
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 68
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 79
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 66
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 76
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 77
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 78
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 84
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 78
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 76
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 65
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 90
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 65
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 68
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 65
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r1:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r2
    feq.d t3, ft0, ft4
    bne t3, x0, L_r2
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r1
L_r2:
    li a0, 10
    li a7, 11
    ecall
    la t0, cst2
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 177
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et117:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 177
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst8
    fld fa1, 0(t0)
    flt.d t0, fa0, fa1
    bne t0, x0, et118
    j et120
et118:
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 177
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst9
    fld fa1, 0(t0)
    fmul.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_insertarFinal_a1
L_ret_3:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 23
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et119:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 177
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 24
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 24
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 25
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 25
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 177
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et117
et120:
    la t0, stack
    li t1, 48
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 26
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 26
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst10
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst11
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 182
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst6
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_insertarFinal_a2
L_ret_4:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 27
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 28
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 76
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 40
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 41
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 28
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r3:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r4
    feq.d t3, ft0, ft4
    bne t3, x0, L_r4
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r3
L_r4:
    li a0, 10
    li a7, 11
    ecall
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 29
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 29
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst12
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_imprimirAdelante_a0
L_ret_5:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 30
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 31
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 76
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 40
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 41
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 31
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r5:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r6
    feq.d t3, ft0, ft4
    bne t3, x0, L_r6
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r5
L_r6:
    li a0, 10
    li a7, 11
    ecall
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 32
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 32
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst8
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_imprimirAtras_a0
L_ret_6:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 33
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 34
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 79
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 34
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r7:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r8
    feq.d t3, ft0, ft4
    bne t3, x0, L_r8
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r7
L_r8:
    li a0, 10
    li a7, 11
    ecall
    la t0, stack
    li t1, 48
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 35
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 35
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst5
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_imprimirAdelante_a0
L_ret_7:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 36
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et121:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 37
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 37
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r9:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r10
    feq.d t3, ft0, ft4
    bne t3, x0, L_r10
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r9
L_r10:
    li a0, 10
    li a7, 11
    ecall
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 38
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 49
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 73
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 50
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 73
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 102
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 51
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 73
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 112
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 38
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r11:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r12
    feq.d t3, ft0, ft4
    bne t3, x0, L_r12
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r11
L_r12:
    li a0, 10
    li a7, 11
    ecall
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 39
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 52
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 53
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 102
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 54
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 112
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 39
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r13:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r14
    feq.d t3, ft0, ft4
    bne t3, x0, L_r14
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r13
L_r14:
    li a0, 10
    li a7, 11
    ecall
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 40
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 55
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 112
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 118
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 56
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 66
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 117
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 118
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 57
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 73
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 118
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 40
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r15:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r16
    feq.d t3, ft0, ft4
    bne t3, x0, L_r16
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r15
L_r16:
    li a0, 10
    li a7, 11
    ecall
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 41
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 49
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 48
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 79
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 49
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 49
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 73
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 112
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 49
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 50
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 41
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r17:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r18
    feq.d t3, ft0, ft4
    bne t3, x0, L_r18
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r17
L_r18:
    li a0, 10
    li a7, 11
    ecall
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 42
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 49
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 51
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 86
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 48
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 83
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 42
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r19:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r20
    feq.d t3, ft0, ft4
    bne t3, x0, L_r20
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r19
L_r20:
    li a0, 10
    li a7, 11
    ecall
    li a7, 7
    ecall
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 43
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 43
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, stack
    fsd fa0, 0(t0)
    la t0, stack
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 44
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 44
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et125
    j et126
et125:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 45
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 86
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 45
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r21:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r22
    feq.d t3, ft0, ft4
    bne t3, x0, L_r22
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r21
L_r22:
    li a0, 10
    li a7, 11
    ecall
    li a7, 7
    ecall
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 46
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 46
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 8
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 47
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 8
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 48
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 47
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 48
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst10
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_insertarInicio_a1
L_ret_8:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 49
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 56
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 50
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 50
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst2
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst11
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_registrar
L_ret_9:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 51
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et124
et126:
    la t0, stack
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 52
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 52
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et127
    j et128
et127:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 53
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 86
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 53
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r23:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r24
    feq.d t3, ft0, ft4
    bne t3, x0, L_r24
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r23
L_r24:
    li a0, 10
    li a7, 11
    ecall
    li a7, 7
    ecall
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 54
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 54
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 8
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 55
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 8
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 56
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 55
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 56
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst9
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_insertarFinal_a1
L_ret_10:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 57
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 56
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 58
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 58
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst2
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst13
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_registrar
L_ret_11:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 59
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et124
et128:
    la t0, stack
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 60
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 60
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst1
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et129
    j et130
et129:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 61
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 80
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 61
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r25:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r26
    feq.d t3, ft0, ft4
    bne t3, x0, L_r26
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r25
L_r26:
    li a0, 10
    li a7, 11
    ecall
    li a7, 7
    ecall
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 62
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 62
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 16
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 63
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 86
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 63
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r27:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r28
    feq.d t3, ft0, ft4
    bne t3, x0, L_r28
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r27
L_r28:
    li a0, 10
    li a7, 11
    ecall
    li a7, 7
    ecall
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 64
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 64
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 8
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 65
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 16
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 66
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 8
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 67
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 65
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 66
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 67
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 182
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst14
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_insertarEn_a2
L_ret_12:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 68
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 68
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 32
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 32
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 69
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 69
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et132
    j et133
et132:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 70
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 73
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 112
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 70
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r29:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r30
    feq.d t3, ft0, ft4
    bne t3, x0, L_r30
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r29
L_r30:
    la t0, stack
    li t1, 16
    add t0, t0, t1
    fld fa0, 0(t0)
    fcvt.w.d a0, fa0
    li a7, 1
    ecall
    li a0, 10
    li a7, 11
    ecall
    la t0, stack
    li t1, 56
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 71
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 71
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst2
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst15
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_registrar
L_ret_13:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 72
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et131
et133:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 73
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 80
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 118
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 73
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r31:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r32
    feq.d t3, ft0, ft4
    bne t3, x0, L_r32
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r31
L_r32:
    li a0, 10
    li a7, 11
    ecall
et131:
    j et124
et130:
    la t0, stack
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 74
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 74
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst6
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et134
    j et135
et134:
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 75
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 75
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst16
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_eliminarInicio_a0
L_ret_14:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 76
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 76
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    beq t0, x0, et137
    j et138
et137:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 77
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 77
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r33:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r34
    feq.d t3, ft0, ft4
    bne t3, x0, L_r34
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r33
L_r34:
    li a0, 10
    li a7, 11
    ecall
    la t0, stack
    li t1, 56
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 78
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 78
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst3
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst17
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_registrar
L_ret_15:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 79
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et136
et138:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 80
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 76
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 118
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 80
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r35:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r36
    feq.d t3, ft0, ft4
    bne t3, x0, L_r36
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r35
L_r36:
    li a0, 10
    li a7, 11
    ecall
et136:
    j et124
et135:
    la t0, stack
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 81
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 81
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst12
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et139
    j et140
et139:
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 82
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 82
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst18
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_eliminarFinal_a0
L_ret_16:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 83
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 83
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    beq t0, x0, et142
    j et143
et142:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 84
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 102
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 84
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r37:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r38
    feq.d t3, ft0, ft4
    bne t3, x0, L_r38
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r37
L_r38:
    li a0, 10
    li a7, 11
    ecall
    la t0, stack
    li t1, 56
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 85
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 85
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst3
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst19
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_registrar
L_ret_17:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 86
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et141
et143:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 87
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 76
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 118
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 87
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r39:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r40
    feq.d t3, ft0, ft4
    bne t3, x0, L_r40
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r39
L_r40:
    li a0, 10
    li a7, 11
    ecall
et141:
    j et124
et140:
    la t0, stack
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 88
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 88
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst8
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et144
    j et145
et144:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 89
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 80
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 89
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r41:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r42
    feq.d t3, ft0, ft4
    bne t3, x0, L_r42
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r41
L_r42:
    li a0, 10
    li a7, 11
    ecall
    li a7, 7
    ecall
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 90
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 90
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 16
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 91
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 16
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 92
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 91
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 92
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst20
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_eliminarEn_a1
L_ret_18:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 93
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 93
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 32
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 32
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 94
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 94
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et147
    j et148
et147:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 95
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 112
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 95
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r43:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r44
    feq.d t3, ft0, ft4
    bne t3, x0, L_r44
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r43
L_r44:
    la t0, stack
    li t1, 16
    add t0, t0, t1
    fld fa0, 0(t0)
    fcvt.w.d a0, fa0
    li a7, 1
    ecall
    li a0, 10
    li a7, 11
    ecall
    la t0, stack
    li t1, 56
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 96
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 96
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst3
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst21
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_registrar
L_ret_19:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 97
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et146
et148:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 98
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 80
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 118
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 98
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r45:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r46
    feq.d t3, ft0, ft4
    bne t3, x0, L_r46
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r45
L_r46:
    li a0, 10
    li a7, 11
    ecall
et146:
    j et124
et145:
    la t0, stack
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 99
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 99
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst5
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et149
    j et150
et149:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 100
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 86
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 100
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r47:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r48
    feq.d t3, ft0, ft4
    bne t3, x0, L_r48
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r47
L_r48:
    li a0, 10
    li a7, 11
    ecall
    li a7, 7
    ecall
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 101
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 101
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 8
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 102
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 8
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 103
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 102
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 103
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst22
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_eliminarValor_a1
L_ret_20:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 104
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 104
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 32
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 32
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 105
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 105
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et152
    j et153
et152:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 106
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 86
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 106
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r49:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r50
    feq.d t3, ft0, ft4
    bne t3, x0, L_r50
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r49
L_r50:
    la t0, stack
    li t1, 8
    add t0, t0, t1
    fld fa0, 0(t0)
    fcvt.w.d a0, fa0
    li a7, 1
    ecall
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 107
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 107
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r51:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r52
    feq.d t3, ft0, ft4
    bne t3, x0, L_r52
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r51
L_r52:
    li a0, 10
    li a7, 11
    ecall
    la t0, stack
    li t1, 56
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 108
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 108
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst3
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst23
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_registrar
L_ret_21:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 109
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et151
et153:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 110
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 118
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 120
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 110
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r53:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r54
    feq.d t3, ft0, ft4
    bne t3, x0, L_r54
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r53
L_r54:
    li a0, 10
    li a7, 11
    ecall
et151:
    j et124
et150:
    la t0, stack
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 111
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 111
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst10
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et154
    j et155
et154:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 112
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 86
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 98
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 117
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 112
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r55:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r56
    feq.d t3, ft0, ft4
    bne t3, x0, L_r56
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r55
L_r56:
    li a0, 10
    li a7, 11
    ecall
    li a7, 7
    ecall
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 113
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 113
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 8
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 114
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 8
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 115
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 114
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 115
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst24
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_buscar_a1
L_ret_22:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 116
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 116
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, stack
    li t1, 24
    add t0, t0, t1
    fsd fa0, 0(t0)
    la t0, stack
    li t1, 24
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 117
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst2
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    fsub.d fa0, fa1, fa0
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 118
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 117
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 118
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et157
    j et158
et157:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 119
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 78
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 119
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r57:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r58
    feq.d t3, ft0, ft4
    bne t3, x0, L_r58
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r57
L_r58:
    li a0, 10
    li a7, 11
    ecall
    j et156
et158:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 120
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 120
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r59:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r60
    feq.d t3, ft0, ft4
    bne t3, x0, L_r60
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r59
L_r60:
    la t0, stack
    li t1, 24
    add t0, t0, t1
    fld fa0, 0(t0)
    fcvt.w.d a0, fa0
    li a7, 1
    ecall
    li a0, 10
    li a7, 11
    ecall
    la t0, stack
    li t1, 56
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 121
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 121
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 181
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst25
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_registrar
L_ret_23:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 122
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et156:
    j et124
et155:
    la t0, stack
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 123
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 123
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst11
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et159
    j et160
et159:
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 124
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 124
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst26
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_invertir_a0
L_ret_24:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 125
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 126
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 76
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 118
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 126
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r61:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r62
    feq.d t3, ft0, ft4
    bne t3, x0, L_r62
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r61
L_r62:
    li a0, 10
    li a7, 11
    ecall
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 127
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 127
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst27
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_imprimirAdelante_a0
L_ret_25:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 128
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et124
et160:
    la t0, stack
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 129
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 129
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst9
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et161
    j et162
et161:
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 130
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 130
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst28
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_ordenar_a0
L_ret_26:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 131
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 132
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 76
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 132
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r63:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r64
    feq.d t3, ft0, ft4
    bne t3, x0, L_r64
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r63
L_r64:
    li a0, 10
    li a7, 11
    ecall
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 133
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 133
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst29
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_imprimirAdelante_a0
L_ret_27:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 134
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et124
et162:
    la t0, stack
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 135
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 135
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst13
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et163
    j et164
et163:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 136
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 65
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 136
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r65:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r66
    feq.d t3, ft0, ft4
    bne t3, x0, L_r66
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r65
L_r66:
    li a0, 10
    li a7, 11
    ecall
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 137
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 137
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst30
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_imprimirAdelante_a0
L_ret_28:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 138
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 139
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 65
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 139
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r67:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r68
    feq.d t3, ft0, ft4
    bne t3, x0, L_r68
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r67
L_r68:
    li a0, 10
    li a7, 11
    ecall
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 140
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 140
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst31
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_imprimirAtras_a0
L_ret_29:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 141
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et124
et164:
    la t0, stack
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 142
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 142
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst14
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et165
    j et166
et165:
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 143
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 143
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst32
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_estaVacia_a0
L_ret_30:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 144
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 144
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    beq t0, x0, et168
    j et169
et168:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 145
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 76
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 118
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 145
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r69:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r70
    feq.d t3, ft0, ft4
    bne t3, x0, L_r70
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r69
L_r70:
    li a0, 10
    li a7, 11
    ecall
    j et167
et169:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 146
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 84
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 146
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r71:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r72
    feq.d t3, ft0, ft4
    bne t3, x0, L_r72
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r71
L_r72:
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 147
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 147
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 148
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 148
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d a0, fa0
    li a7, 1
    ecall
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 149
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 40
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 149
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r73:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r74
    feq.d t3, ft0, ft4
    bne t3, x0, L_r74
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r73
L_r74:
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 150
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 150
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 151
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 151
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst33
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_paridad
L_ret_31:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 152
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 152
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r75:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r76
    feq.d t3, ft0, ft4
    bne t3, x0, L_r76
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r75
L_r76:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 153
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 41
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 153
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r77:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r78
    feq.d t3, ft0, ft4
    bne t3, x0, L_r78
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r77
L_r78:
    li a0, 10
    li a7, 11
    ecall
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 154
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 83
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 117
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 154
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r79:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r80
    feq.d t3, ft0, ft4
    bne t3, x0, L_r80
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r79
L_r80:
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 155
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 155
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst34
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_suma_a0
L_ret_32:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 156
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 156
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d a0, fa0
    li a7, 1
    ecall
    li a0, 10
    li a7, 11
    ecall
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 157
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 77
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 120
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 157
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r81:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r82
    feq.d t3, ft0, ft4
    bne t3, x0, L_r82
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r81
L_r82:
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 158
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 158
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst35
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_maximo_a0
L_ret_33:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 159
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 159
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d a0, fa0
    li a7, 1
    ecall
    li a0, 10
    li a7, 11
    ecall
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 160
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 80
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 160
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r83:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r84
    feq.d t3, ft0, ft4
    bne t3, x0, L_r84
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r83
L_r84:
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 161
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 161
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 162
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 162
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d a0, fa0
    li a7, 1
    ecall
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 163
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 124
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 85
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 163
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r85:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r86
    feq.d t3, ft0, ft4
    bne t3, x0, L_r86
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r85
L_r86:
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 164
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 164
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 165
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 165
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 166
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 166
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d a0, fa0
    li a7, 1
    ecall
    li a0, 10
    li a7, 11
    ecall
et167:
    j et124
et166:
    la t0, stack
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 167
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 167
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst15
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et170
    j et171
et170:
    la t0, stack
    li t1, 40
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 168
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 168
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst36
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_vaciar_a0
L_ret_34:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 169
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 170
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 76
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 118
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 170
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r87:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r88
    feq.d t3, ft0, ft4
    bne t3, x0, L_r88
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r87
L_r88:
    li a0, 10
    li a7, 11
    ecall
    j et124
et171:
    la t0, stack
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 171
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 171
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et172
    j et173
et172:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 172
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 83
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 172
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r89:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r90
    feq.d t3, ft0, ft4
    bne t3, x0, L_r90
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r89
L_r90:
    li a0, 10
    li a7, 11
    ecall
    j et124
et173:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 173
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 79
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 112
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 118
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 173
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r91:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r92
    feq.d t3, ft0, ft4
    bne t3, x0, L_r92
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r91
L_r92:
    li a0, 10
    li a7, 11
    ecall
et124:
et122:
    la t0, stack
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 174
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 174
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    beq t0, x0, et121
    j et123
et123:
    la t0, stack
    li t1, 56
    add t0, t0, t1
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 175
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 175
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 180
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst37
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 178
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 179
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst4
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_imprimirRegistro
L_ret_35:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 176
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j L_end
LF_Nodo_Nodo_a1:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_ListaDoble_a0:
    la t0, cst7
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_ListaDoble_a1:
    la t0, cst7
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst38
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst11
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_insertarFinal_a1
L_ret_36:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_estaVacia_a0:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et1
    j et2
et1:
    la t0, cst2
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et3
et2:
    la t0, cst0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et3:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_insertarInicio_a1:
    la t0, H
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst1
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, H
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, otype
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst39
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst21
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_Nodo_Nodo_a1
L_ret_37:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et5
    j et6
et5:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    j et4
et6:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
et4:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_insertarFinal_a1:
    la t0, H
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst1
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, H
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, otype
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 25
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 26
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst40
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 23
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 24
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst25
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_Nodo_Nodo_a1
L_ret_38:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et8
    j et9
et8:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    j et7
et9:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
et7:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_insertarFinal_a2:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst41
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst13
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_insertarFinal_a1
L_ret_39:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst42
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst13
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_insertarFinal_a1
L_ret_40:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_insertarEn_a2:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    flt.d t0, fa0, fa1
    bne t0, x0, et11
    j et13
et13:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    flt.d t0, fa1, fa0
    bne t0, x0, et11
    j et12
et11:
    la t0, cst0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    j et10
et12:
et10:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et15
    j et16
et15:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 42
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 43
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst43
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 40
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 41
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst42
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_insertarInicio_a1
L_ret_41:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst2
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    j et14
et16:
et14:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et18
    j et19
et18:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 42
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 43
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst44
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 40
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 41
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst42
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_insertarFinal_a1
L_ret_42:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst2
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    j et17
et19:
et17:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 42
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 43
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst45
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 40
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 41
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst42
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_obtenerNodo_a1
L_ret_43:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 38
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, H
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst1
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 23
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 23
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, H
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 24
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 24
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 25
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 25
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, otype
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 26
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 42
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 26
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 43
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst46
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 40
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 41
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst42
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_Nodo_Nodo_a1
L_ret_44:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 39
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 39
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 27
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 38
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 28
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 28
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 29
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 29
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 27
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 39
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 30
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 38
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 30
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 38
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 31
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 31
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 32
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 32
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 33
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 39
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 33
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 38
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 34
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 39
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 34
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 35
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 35
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 36
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 36
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 37
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 37
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 35
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst2
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_eliminarInicio_a0:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et21
    j et22
et21:
    la t0, cst0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    j et20
et22:
et20:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et24
    j et25
et24:
    la t0, cst7
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    j et23
et25:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
et23:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fsub.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst2
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_eliminarFinal_a0:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et27
    j et28
et27:
    la t0, cst0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    j et26
et28:
et26:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et30
    j et31
et30:
    la t0, cst7
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    j et29
et31:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
et29:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fsub.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst2
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_eliminarEn_a1:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    flt.d t0, fa0, fa1
    bne t0, x0, et33
    j et35
et35:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    fle.d t0, fa1, fa0
    bne t0, x0, et33
    j et34
et33:
    la t0, cst0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    j et32
et34:
et32:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et37
    j et38
et37:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 36
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst47
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 34
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 35
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst36
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_eliminarInicio_a0
L_ret_45:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    j et36
et38:
et36:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fsub.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et40
    j et41
et40:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 36
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst48
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 34
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 35
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst36
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_eliminarFinal_a0
L_ret_46:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    j et39
et41:
et39:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 36
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 37
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst49
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 34
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 35
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst36
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_obtenerNodo_a1
L_ret_47:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 33
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 33
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 33
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 23
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 23
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 24
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 24
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 33
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 25
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 25
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 26
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 26
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 27
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 33
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 28
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 28
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 29
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 29
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 27
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 30
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 30
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 31
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 31
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fsub.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 32
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 32
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 30
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst2
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_eliminarValor_a1:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst50
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst15
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_buscar_a1
L_ret_48:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst2
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    fsub.d fa0, fa1, fa0
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et43
    j et44
et43:
    la t0, cst0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    j et42
et44:
et42:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst51
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst15
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_eliminarEn_a1
L_ret_49:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_vaciar_a0:
    la t0, cst7
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_obtenerNodo_a1:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    flt.d t0, fa0, fa1
    bne t0, x0, et46
    j et48
et48:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    fle.d t0, fa1, fa0
    bne t0, x0, et46
    j et47
et46:
    la t0, cst7
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    j et45
et47:
et45:
    la t0, cst7
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 30
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fdiv.d fa0, fa0, fa1
    fcvt.w.d t0, fa0
    fcvt.d.w fa0, t0
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    flt.d t0, fa0, fa1
    bne t0, x0, et50
    j et51
et50:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 30
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 28
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et52:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 28
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    flt.d t0, fa0, fa1
    bne t0, x0, et53
    j et55
et53:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 30
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 30
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et54:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 28
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 28
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et52
et55:
    j et49
et51:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 30
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fsub.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 29
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et56:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 29
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 23
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 23
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    flt.d t0, fa1, fa0
    bne t0, x0, et57
    j et59
et57:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 30
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 24
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 24
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 25
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 25
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 30
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et58:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 29
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 26
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 26
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fsub.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 27
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 27
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 29
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et56
et59:
et49:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 30
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_obtener_a1:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst52
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst14
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_obtenerNodo_a1
L_ret_50:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et60
    j et61
et60:
    la t0, cst2
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    fsub.d fa0, fa1, fa0
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et62
et61:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et62:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_buscar_a1:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et63:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    beq t0, x0, et64
    j et65
et64:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et67
    j et68
et67:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    j et66
et68:
et66:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et63
et65:
    la t0, cst2
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    fsub.d fa0, fa1, fa0
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_maximo_a0:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et70
    j et71
et70:
    la t0, cst0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    j et69
et71:
et69:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et72:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    beq t0, x0, et73
    j et74
et73:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    flt.d t0, fa1, fa0
    bne t0, x0, et76
    j et77
et76:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et75
et77:
et75:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et72
et74:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_sumaRecursiva_a1:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et79
    j et80
et79:
    la t0, cst0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    j et78
et80:
et78:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst53
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst13
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_sumaRecursiva_a1
L_ret_51:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_suma_a0:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst54
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst8
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_ListaDoble_sumaRecursiva_a1
L_ret_52:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_invertir_a0:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst7
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et81:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    beq t0, x0, et82
    j et83
et82:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et81
et83:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_ordenar_a0:
et84:
    la t0, cst0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et87:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    beq t0, x0, et90
    j et89
et90:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    beq t0, x0, et88
    j et89
et88:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    flt.d t0, fa1, fa0
    bne t0, x0, et92
    j et93
et92:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, cst2
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et91
et93:
et91:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et87
et89:
et85:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    beq t0, x0, et84
    j et86
et86:
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_imprimirAdelante_a0:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 91
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et94:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    beq t0, x0, et95
    j et96
et95:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst9
    fld ft6, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    flt.d t0, fa0, ft3
    beq t0, x0, L_r93
    li a0, 45
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    fsub.d fa0, ft3, fa0
L_r93:
    fcvt.w.d s3, fa0
    fcvt.d.w ft4, s3
    fsub.d ft5, fa0, ft4
    li s1, 1
    mv s4, s3
    li s5, 10
L_r94:
    blt s4, s5, L_r95
    div s4, s4, s5
    addi s1, s1, 1
    j L_r94
L_r95:
    li s2, 1
    addi s4, s1, -1
L_r99:
    beq s4, x0, L_r96
    mul s2, s2, s5
    addi s4, s4, -1
    j L_r99
L_r96:
L_r97:
    beq s2, x0, L_r98
    div a0, s3, s2
    addi a0, a0, 48
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    rem s3, s3, s2
    div s2, s2, s5
    j L_r97
L_r98:
    feq.d t0, ft5, ft3
    bne t0, x0, L_r100
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li s6, 0
L_r101:
    fmul.d ft5, ft5, ft6
    fcvt.w.d a0, ft5
    fcvt.d.w ft7, a0
    fsub.d ft5, ft5, ft7
    addi a0, a0, 48
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s6, s6, 1
    feq.d t0, ft5, ft3
    bne t0, x0, L_r100
    li t1, 12
    blt s6, t1, L_r101
L_r100:
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r104:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r102
    feq.d t3, ft0, ft4
    bne t3, x0, L_r102
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r104
L_r102:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r105:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r103
    feq.d t3, ft0, ft4
    bne t3, x0, L_r103
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r105
L_r103:
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft3, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    beq t0, x0, et98
    j et99
et98:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 60
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 45
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 62
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r108:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r106
    feq.d t3, ft0, ft4
    bne t3, x0, L_r106
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r108
L_r106:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r109:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r107
    feq.d t3, ft0, ft4
    bne t3, x0, L_r107
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r109
L_r107:
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft3, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et97
et99:
et97:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et94
et96:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 93
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r112:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r110
    feq.d t3, ft0, ft4
    bne t3, x0, L_r110
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r112
L_r110:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r113:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r111
    feq.d t3, ft0, ft4
    bne t3, x0, L_r111
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r113
L_r111:
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft3, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r114:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r115
    feq.d t3, ft0, ft4
    bne t3, x0, L_r115
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r114
L_r115:
    li a0, 10
    li a7, 11
    ecall
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_ListaDoble_imprimirAtras_a0:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 91
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et100:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    beq t0, x0, et101
    j et102
et101:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst9
    fld ft6, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    flt.d t0, fa0, ft3
    beq t0, x0, L_r116
    li a0, 45
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    fsub.d fa0, ft3, fa0
L_r116:
    fcvt.w.d s3, fa0
    fcvt.d.w ft4, s3
    fsub.d ft5, fa0, ft4
    li s1, 1
    mv s4, s3
    li s5, 10
L_r117:
    blt s4, s5, L_r118
    div s4, s4, s5
    addi s1, s1, 1
    j L_r117
L_r118:
    li s2, 1
    addi s4, s1, -1
L_r122:
    beq s4, x0, L_r119
    mul s2, s2, s5
    addi s4, s4, -1
    j L_r122
L_r119:
L_r120:
    beq s2, x0, L_r121
    div a0, s3, s2
    addi a0, a0, 48
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    rem s3, s3, s2
    div s2, s2, s5
    j L_r120
L_r121:
    feq.d t0, ft5, ft3
    bne t0, x0, L_r123
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li s6, 0
L_r124:
    fmul.d ft5, ft5, ft6
    fcvt.w.d a0, ft5
    fcvt.d.w ft7, a0
    fsub.d ft5, ft5, ft7
    addi a0, a0, 48
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s6, s6, 1
    feq.d t0, ft5, ft3
    bne t0, x0, L_r123
    li t1, 12
    blt s6, t1, L_r124
L_r123:
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r127:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r125
    feq.d t3, ft0, ft4
    bne t3, x0, L_r125
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r127
L_r125:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r128:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r126
    feq.d t3, ft0, ft4
    bne t3, x0, L_r126
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r128
L_r126:
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft3, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst7
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    beq t0, x0, et104
    j et105
et104:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 60
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 45
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 62
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r131:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r129
    feq.d t3, ft0, ft4
    bne t3, x0, L_r129
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r131
L_r129:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r132:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r130
    feq.d t3, ft0, ft4
    bne t3, x0, L_r130
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r132
L_r130:
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft3, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et103
et105:
et103:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et100
et102:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 93
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r135:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r133
    feq.d t3, ft0, ft4
    bne t3, x0, L_r133
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r135
L_r133:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r136:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r134
    feq.d t3, ft0, ft4
    bne t3, x0, L_r134
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r136
L_r134:
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft3, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r137:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r138
    feq.d t3, ft0, ft4
    bne t3, x0, L_r138
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r137
L_r138:
    li a0, 10
    li a7, 11
    ecall
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_registrar:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et108
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et109
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst1
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et110
    j et107
et108:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    j et106
    j et106
et109:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    j et106
    j et106
et110:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst1
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fsd fa1, 0(t0)
    j et106
    j et106
et107:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 84
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 112
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 112
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r139:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r140
    feq.d t3, ft0, ft4
    bne t3, x0, L_r140
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r139
L_r140:
    li a0, 10
    li a7, 11
    ecall
    j et106
et106:
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_esPar:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fdiv.d fa0, fa0, fa1
    fcvt.w.d t0, fa0
    fcvt.d.w fa0, t0
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fmul.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa1, 0(t0)
    fsub.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst0
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et111
    j et112
et111:
    la t0, cst2
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    j et113
et112:
    la t0, cst0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
et113:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_paridad:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, cst55
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld fa0, 0(t0)
    la t0, cst5
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fsd fa0, 0(t0)
    j LF_esPar
L_ret_53:
    la t0, RET
    fld fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    feq.d t0, fa0, fa1
    bne t0, x0, et115
    j et116
et115:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 112
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
    j et114
et116:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 112
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RET
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
et114:
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
LF_imprimirRegistro:
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 82
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 71
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 73
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 83
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 84
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 82
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 79
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 68
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 79
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 80
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 82
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 65
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 67
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 73
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 79
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 78
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 83
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 61
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 3
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r141:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r142
    feq.d t3, ft0, ft4
    bne t3, x0, L_r142
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r141
L_r142:
    li a0, 10
    li a7, 11
    ecall
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 79
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 112
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst9
    fld ft6, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 5
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    flt.d t0, fa0, ft3
    beq t0, x0, L_r143
    li a0, 45
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    fsub.d fa0, ft3, fa0
L_r143:
    fcvt.w.d s3, fa0
    fcvt.d.w ft4, s3
    fsub.d ft5, fa0, ft4
    li s1, 1
    mv s4, s3
    li s5, 10
L_r144:
    blt s4, s5, L_r145
    div s4, s4, s5
    addi s1, s1, 1
    j L_r144
L_r145:
    li s2, 1
    addi s4, s1, -1
L_r149:
    beq s4, x0, L_r146
    mul s2, s2, s5
    addi s4, s4, -1
    j L_r149
L_r146:
L_r147:
    beq s2, x0, L_r148
    div a0, s3, s2
    addi a0, a0, 48
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    rem s3, s3, s2
    div s2, s2, s5
    j L_r147
L_r148:
    feq.d t0, ft5, ft3
    bne t0, x0, L_r150
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li s6, 0
L_r151:
    fmul.d ft5, ft5, ft6
    fcvt.w.d a0, ft5
    fcvt.d.w ft7, a0
    fsub.d ft5, ft5, ft7
    addi a0, a0, 48
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s6, s6, 1
    feq.d t0, ft5, ft3
    bne t0, x0, L_r150
    li t1, 12
    blt s6, t1, L_r151
L_r150:
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 4
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r154:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r152
    feq.d t3, ft0, ft4
    bne t3, x0, L_r152
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r154
L_r152:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 6
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r155:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r153
    feq.d t3, ft0, ft4
    bne t3, x0, L_r153
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r155
L_r153:
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft3, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 7
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r156:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r157
    feq.d t3, ft0, ft4
    bne t3, x0, L_r157
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r156
L_r157:
    li a0, 10
    li a7, 11
    ecall
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 73
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 114
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst2
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 9
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst9
    fld ft6, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 10
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    flt.d t0, fa0, ft3
    beq t0, x0, L_r158
    li a0, 45
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    fsub.d fa0, ft3, fa0
L_r158:
    fcvt.w.d s3, fa0
    fcvt.d.w ft4, s3
    fsub.d ft5, fa0, ft4
    li s1, 1
    mv s4, s3
    li s5, 10
L_r159:
    blt s4, s5, L_r160
    div s4, s4, s5
    addi s1, s1, 1
    j L_r159
L_r160:
    li s2, 1
    addi s4, s1, -1
L_r164:
    beq s4, x0, L_r161
    mul s2, s2, s5
    addi s4, s4, -1
    j L_r164
L_r161:
L_r162:
    beq s2, x0, L_r163
    div a0, s3, s2
    addi a0, a0, 48
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    rem s3, s3, s2
    div s2, s2, s5
    j L_r162
L_r163:
    feq.d t0, ft5, ft3
    bne t0, x0, L_r165
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li s6, 0
L_r166:
    fmul.d ft5, ft5, ft6
    fcvt.w.d a0, ft5
    fcvt.d.w ft7, a0
    fsub.d ft5, ft5, ft7
    addi a0, a0, 48
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s6, s6, 1
    feq.d t0, ft5, ft3
    bne t0, x0, L_r165
    li t1, 12
    blt s6, t1, L_r166
L_r165:
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 8
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r169:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r167
    feq.d t3, ft0, ft4
    bne t3, x0, L_r167
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r169
L_r167:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 11
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r170:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r168
    feq.d t3, ft0, ft4
    bne t3, x0, L_r168
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r170
L_r168:
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft3, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 12
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r171:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r172
    feq.d t3, ft0, ft4
    bne t3, x0, L_r172
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r171
L_r172:
    li a0, 10
    li a7, 11
    ecall
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 69
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 108
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 109
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 99
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 110
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst3
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 14
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst9
    fld ft6, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 15
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    flt.d t0, fa0, ft3
    beq t0, x0, L_r173
    li a0, 45
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    fsub.d fa0, ft3, fa0
L_r173:
    fcvt.w.d s3, fa0
    fcvt.d.w ft4, s3
    fsub.d ft5, fa0, ft4
    li s1, 1
    mv s4, s3
    li s5, 10
L_r174:
    blt s4, s5, L_r175
    div s4, s4, s5
    addi s1, s1, 1
    j L_r174
L_r175:
    li s2, 1
    addi s4, s1, -1
L_r179:
    beq s4, x0, L_r176
    mul s2, s2, s5
    addi s4, s4, -1
    j L_r179
L_r176:
L_r177:
    beq s2, x0, L_r178
    div a0, s3, s2
    addi a0, a0, 48
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    rem s3, s3, s2
    div s2, s2, s5
    j L_r177
L_r178:
    feq.d t0, ft5, ft3
    bne t0, x0, L_r180
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li s6, 0
L_r181:
    fmul.d ft5, ft5, ft6
    fcvt.w.d a0, ft5
    fcvt.d.w ft7, a0
    fsub.d ft5, ft5, ft7
    addi a0, a0, 48
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s6, s6, 1
    feq.d t0, ft5, ft3
    bne t0, x0, L_r180
    li t1, 12
    blt s6, t1, L_r181
L_r180:
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 13
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r184:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r182
    feq.d t3, ft0, ft4
    bne t3, x0, L_r182
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r184
L_r182:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 16
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r185:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r183
    feq.d t3, ft0, ft4
    bne t3, x0, L_r183
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r185
L_r183:
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft3, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 17
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r186:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r187
    feq.d t3, ft0, ft4
    bne t3, x0, L_r187
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r186
L_r187:
    li a0, 10
    li a7, 11
    ecall
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    li a0, 66
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 117
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 113
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 117
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 100
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 101
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 120
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 105
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 116
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 111
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 97
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 115
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 58
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 32
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 2
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, cst1
    fld fa1, 0(t0)
    fadd.d fa0, fa0, fa1
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 19
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t0, fa0
    slli t0, t0, 3
    la t1, heap
    add t0, t1, t0
    fld fa1, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd fa1, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst9
    fld ft6, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 20
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    flt.d t0, fa0, ft3
    beq t0, x0, L_r188
    li a0, 45
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    fsub.d fa0, ft3, fa0
L_r188:
    fcvt.w.d s3, fa0
    fcvt.d.w ft4, s3
    fsub.d ft5, fa0, ft4
    li s1, 1
    mv s4, s3
    li s5, 10
L_r189:
    blt s4, s5, L_r190
    div s4, s4, s5
    addi s1, s1, 1
    j L_r189
L_r190:
    li s2, 1
    addi s4, s1, -1
L_r194:
    beq s4, x0, L_r191
    mul s2, s2, s5
    addi s4, s4, -1
    j L_r194
L_r191:
L_r192:
    beq s2, x0, L_r193
    div a0, s3, s2
    addi a0, a0, 48
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    rem s3, s3, s2
    div s2, s2, s5
    j L_r192
L_r193:
    feq.d t0, ft5, ft3
    bne t0, x0, L_r195
    li a0, 46
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    li s6, 0
L_r196:
    fmul.d ft5, ft5, ft6
    fcvt.w.d a0, ft5
    fcvt.d.w ft7, a0
    fsub.d ft5, ft5, ft7
    addi a0, a0, 48
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s6, s6, 1
    feq.d t0, ft5, ft3
    bne t0, x0, L_r195
    li t1, 12
    blt s6, t1, L_r196
L_r195:
    li a0, 0
    la t0, H
    fld ft0, 0(t0)
    fcvt.w.d t1, ft0
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fcvt.d.w ft1, a0
    fsd ft1, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, H
    fld ft2, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 18
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r199:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r197
    feq.d t3, ft0, ft4
    bne t3, x0, L_r197
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r199
L_r197:
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 21
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d s0, fa0
L_r200:
    slli t0, s0, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r198
    feq.d t3, ft0, ft4
    bne t3, x0, L_r198
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft0, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    addi s0, s0, 1
    j L_r200
L_r198:
    la t0, H
    fld ft1, 0(t0)
    fcvt.w.d t1, ft1
    slli t1, t1, 3
    la t2, heap
    add t0, t2, t1
    fsd ft3, 0(t0)
    la t0, H
    fld ft0, 0(t0)
    la t0, cst2
    fld ft1, 0(t0)
    fadd.d ft0, ft0, ft1
    la t0, H
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fsd ft2, 0(t0)
    la t0, cst0
    fld ft3, 0(t0)
    la t0, cst7
    fld ft4, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 22
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    fcvt.w.d t1, fa0
L_r201:
    slli t0, t1, 3
    la t2, heap
    add t0, t2, t0
    fld ft0, 0(t0)
    feq.d t3, ft0, ft3
    bne t3, x0, L_r202
    feq.d t3, ft0, ft4
    bne t3, x0, L_r202
    fcvt.w.d a0, ft0
    li a7, 11
    ecall
    addi t1, t1, 1
    j L_r201
L_r202:
    li a0, 10
    li a7, 11
    ecall
    la t0, cst0
    fld ft0, 0(t0)
    la t0, RET
    fsd ft0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, RA
    fsd fa0, 0(t0)
    la t0, P
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    addi t0, t0, 1
    slli t0, t0, 3
    la t1, stack
    add t0, t1, t0
    fld fa0, 0(t0)
    la t0, P
    fsd fa0, 0(t0)
    j L_dispatch
L_dispatch:
    la t0, RA
    fld ft0, 0(t0)
    fcvt.w.d t0, ft0
    li t1, 1
    beq t0, t1, L_ret_1
    li t1, 2
    beq t0, t1, L_ret_2
    li t1, 3
    beq t0, t1, L_ret_3
    li t1, 4
    beq t0, t1, L_ret_4
    li t1, 5
    beq t0, t1, L_ret_5
    li t1, 6
    beq t0, t1, L_ret_6
    li t1, 7
    beq t0, t1, L_ret_7
    li t1, 8
    beq t0, t1, L_ret_8
    li t1, 9
    beq t0, t1, L_ret_9
    li t1, 10
    beq t0, t1, L_ret_10
    li t1, 11
    beq t0, t1, L_ret_11
    li t1, 12
    beq t0, t1, L_ret_12
    li t1, 13
    beq t0, t1, L_ret_13
    li t1, 14
    beq t0, t1, L_ret_14
    li t1, 15
    beq t0, t1, L_ret_15
    li t1, 16
    beq t0, t1, L_ret_16
    li t1, 17
    beq t0, t1, L_ret_17
    li t1, 18
    beq t0, t1, L_ret_18
    li t1, 19
    beq t0, t1, L_ret_19
    li t1, 20
    beq t0, t1, L_ret_20
    li t1, 21
    beq t0, t1, L_ret_21
    li t1, 22
    beq t0, t1, L_ret_22
    li t1, 23
    beq t0, t1, L_ret_23
    li t1, 24
    beq t0, t1, L_ret_24
    li t1, 25
    beq t0, t1, L_ret_25
    li t1, 26
    beq t0, t1, L_ret_26
    li t1, 27
    beq t0, t1, L_ret_27
    li t1, 28
    beq t0, t1, L_ret_28
    li t1, 29
    beq t0, t1, L_ret_29
    li t1, 30
    beq t0, t1, L_ret_30
    li t1, 31
    beq t0, t1, L_ret_31
    li t1, 32
    beq t0, t1, L_ret_32
    li t1, 33
    beq t0, t1, L_ret_33
    li t1, 34
    beq t0, t1, L_ret_34
    li t1, 35
    beq t0, t1, L_ret_35
    li t1, 36
    beq t0, t1, L_ret_36
    li t1, 37
    beq t0, t1, L_ret_37
    li t1, 38
    beq t0, t1, L_ret_38
    li t1, 39
    beq t0, t1, L_ret_39
    li t1, 40
    beq t0, t1, L_ret_40
    li t1, 41
    beq t0, t1, L_ret_41
    li t1, 42
    beq t0, t1, L_ret_42
    li t1, 43
    beq t0, t1, L_ret_43
    li t1, 44
    beq t0, t1, L_ret_44
    li t1, 45
    beq t0, t1, L_ret_45
    li t1, 46
    beq t0, t1, L_ret_46
    li t1, 47
    beq t0, t1, L_ret_47
    li t1, 48
    beq t0, t1, L_ret_48
    li t1, 49
    beq t0, t1, L_ret_49
    li t1, 50
    beq t0, t1, L_ret_50
    li t1, 51
    beq t0, t1, L_ret_51
    li t1, 52
    beq t0, t1, L_ret_52
    li t1, 53
    beq t0, t1, L_ret_53
    j L_end
L_end:
    li a7, 10
    ecall
