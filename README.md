# 🚗 DriveRent

Sistema de gerenciamento de locação de veículos desenvolvido em Java.

O DriveRent foi desenvolvido como um projeto acadêmico com o objetivo de aplicar conceitos de Programação Orientada a Objetos, separação de responsabilidades, injeção de dependências, tratamento de exceções e modelagem de um sistema de locação de veículos.

---

## 📋 Sobre o projeto

O DriveRent permite gerenciar clientes, veículos e locações através de uma aplicação executada pelo terminal.

O sistema possui três áreas principais:

- 👤 Gerenciamento de clientes
- 🚗 Gerenciamento de veículos
- 📋 Gerenciamento de locações

Além disso, o sistema possui regras de negócio para disponibilidade de veículos, cálculo de valores de locação, multas por atraso, cancelamentos, devoluções e pagamentos.

---

## ✨ Funcionalidades

### 👤 Clientes

O sistema permite:

- Cadastrar clientes
- Buscar cliente por CPF
- Listar todos os clientes
- Atualizar dados do cliente
- Remover cliente
- Validar CPF
- Validar nome
- Validar telefone
- Validar CNH
- Impedir a remoção de clientes que possuem locações ativas

Os dados do cliente incluem:

- Nome
- CPF
- CNH
- Telefone

CPF, CNH e telefone são tratados como `String`, pois são identificadores/dados de contato e não valores utilizados para operações matemáticas.

---

### 🚗 Veículos

O sistema suporta diferentes categorias de veículos:

- 🏍️ Moto
- 🚘 Carro de passeio
- 🚚 Utilitário

Cada tipo possui características específicas.

#### Moto

Possui:

- Cilindradas
- Valor diário
- Regra específica para cálculo da diária

#### Carro de passeio

Possui:

- Número de portas
- Ar-condicionado
- Valor diário

Veículos com ar-condicionado possuem um acréscimo no cálculo da diária.

#### Utilitário

Possui:

- Capacidade de carga em toneladas
- Valor diário

A capacidade de carga influencia o cálculo da diária.

---

### Gerenciamento de veículos

É possível:

- Cadastrar veículo
- Buscar veículo por placa
- Listar veículos
- Atualizar veículo
- Alterar status
- Remover veículo

Os veículos possuem diferentes estados, como:

- `DISPONIVEL`
- `ALUGADO`
- `EM_MANUTENCAO`

O sistema também possui validações para:

- Placa
- Ano
- Valor da diária
- Cilindradas
- Número de portas
- Capacidade de carga

---

### 📋 Locações

O sistema permite:

- Cadastrar locação
- Buscar locações por cliente
- Buscar locações por veículo
- Listar todas as locações
- Atualizar a data de término
- Trocar o veículo de uma locação
- Finalizar locação
- Cancelar locação
- Deletar locação
- Registrar pagamento

Ao cadastrar uma locação:

1. O cliente é verificado.
2. O veículo é verificado.
3. A disponibilidade do veículo é verificada.
4. A data da locação é definida.
5. O valor base da locação é calculado.
6. A locação recebe o status `ATIVA`.
7. O veículo passa para o status `ALUGADO`.

---

### 🔄 Troca de veículo

Durante uma locação ativa é possível trocar o veículo.

O sistema:

- Verifica se a locação está ativa.
- Verifica se o novo veículo existe.
- Verifica se o novo veículo está disponível.
- Calcula os valores referentes aos períodos com cada veículo.
- Libera o veículo anterior.
- Define o novo veículo como alugado.

---

### 🏁 Finalização da locação

Ao finalizar uma locação, o sistema registra a data de devolução e calcula uma possível multa por atraso.

Caso a devolução ocorra após a data prevista, a multa é adicionada ao valor da locação.

Após a finalização:

```text
Locação: ATIVA → CONCLUIDA
Veículo: ALUGADO → DISPONIVEL
