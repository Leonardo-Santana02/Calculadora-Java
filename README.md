# Calculadora Java

Este projeto é uma calculadora **básica e científica**, desenvolvida em **Java** utilizando as bibliotecas **Swing** e **AWT** para a interface gráfica.  
O objetivo é fornecer uma aplicação simples, funcional e extensível, demonstrando conceitos fundamentais de programação orientada a objetos.

---

## 🎯 Funcionalidades

- **Operações básicas**: adição, subtração, multiplicação e divisão.  
- **Constantes matemáticas**: `π` (Pi) e `e` (Euler).  
- **Operações trigonométricas**: `sin`, `cos`, `tan`, `sinh`, `cosh`, `tanh`.  
- **Logaritmos e potências**: logaritmo natural, log base 10, exponenciação.  
- **Funções avançadas**: inverso, fatorial, raízes `n√y`, módulo.  
- **Interface gráfica (GUI)** intuitiva com **Swing**.  
- **Tratamento de exceções** para operações inválidas (como divisão por zero).  
- **Histórico de operações** para consultar cálculos anteriores.  
- **Botão Shift** para exibir funções inversas.  
- **Layout responsivo**: oculta botões científicos quando a janela é reduzida.  

---

## 🖼️ Interface

A calculadora possui uma interface gráfica semelhante a uma calculadora científica comum, implementada com **Swing**.  
> Nota: A aparência pode variar dependendo do sistema operacional e do tema configurado.

---

## 📚 Conceitos de POO Utilizados

O projeto foi construído com foco em boas práticas de **Engenharia de Software**, aplicando:

- **Abstração** → interfaces e classes abstratas para definir operações.  
- **Herança** → classes específicas estendem implementações genéricas.  
- **Polimorfismo** → múltiplas formas de tratar operações matemáticas.  
- **Encapsulamento** → proteção dos atributos internos das classes.  

---

## ⚙️ Requisitos

- **Java 21 ou superior** (uso de métodos modernos do `ArrayList`, como `getLast()`).  
- IDE recomendada: **IntelliJ IDEA**, **Eclipse** ou **NetBeans**.  

---

## 📌 Notas Importantes

- As raízes são representadas no formato `n√y`.  
  - Exemplo: a quarta raiz de 16 → `4√16 = 2`.  
- Ao multiplicar por uma raiz, use `x` explicitamente:  
  - Exemplo: `5 × √4` deve ser escrito como `5x2√4`.  
- Em sistemas **MacOS**, a coloração dos botões pode não renderizar devido à depreciação do OpenGL em versões recentes.  

---

## 🚀 Como Executar

1. Clone este repositório:
   ```bash
   git clone https://github.com/seu-usuario/java-calculator.git
