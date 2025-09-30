 -- File: MyFirstMIPSDatapath.vhd
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity MyFirstMIPSDatapath is
    Port (
        clk        : in  STD_LOGIC;
        i_RST      : in  STD_LOGIC;
        -- Control Signals
        ALUSrc     : in  STD_LOGIC;
        nAdd_Sub   : in  STD_LOGIC;
        reg_write  : in  STD_LOGIC;
        -- Register Addresses
        read_reg1  : in  STD_LOGIC_VECTOR (4 downto 0);
        read_reg2  : in  STD_LOGIC_VECTOR (4 downto 0);
        write_reg  : in  STD_LOGIC_VECTOR (4 downto 0);
        -- Immediate Value
        immediate  : in  STD_LOGIC_VECTOR (31 downto 0);
        -- Outputs (for debugging or further processing)
        read_data1 : out STD_LOGIC_VECTOR (31 downto 0);
        read_data2 : out STD_LOGIC_VECTOR (31 downto 0);
        alu_result : out STD_LOGIC_VECTOR (31 downto 0)
    );
end MyFirstMIPSDatapath;

architecture Structural of MyFirstMIPSDatapath is
    -- Internal Signals
    signal reg_read_data1 : STD_LOGIC_VECTOR (31 downto 0);
    signal reg_read_data2 : STD_LOGIC_VECTOR (31 downto 0);
    signal mux_ALUSrc_out : STD_LOGIC_VECTOR (31 downto 0);
    signal alu_out        : STD_LOGIC_VECTOR (31 downto 0);
    signal alu_carry      : STD_LOGIC;

    -- Component Declarations
    component mipsreg
        Port (
            clk        : in  STD_LOGIC;
            i_RST      : in  STD_LOGIC;
            reg_write  : in  STD_LOGIC;
            read_reg1  : in  STD_LOGIC_VECTOR (4 downto 0);
            read_reg2  : in  STD_LOGIC_VECTOR (4 downto 0);
            write_reg  : in  STD_LOGIC_VECTOR (4 downto 0);
            write_data : in  STD_LOGIC_VECTOR (31 downto 0);
            read_data1 : out STD_LOGIC_VECTOR (31 downto 0);
            read_data2 : out STD_LOGIC_VECTOR (31 downto 0)
        );
    end component;

    component mux2t1_N
        generic (N : integer := 32);
        Port (
            i_S  : in  STD_LOGIC;
            i_D0 : in  STD_LOGIC_VECTOR (N-1 downto 0);
            i_D1 : in  STD_LOGIC_VECTOR (N-1 downto 0);
            o_O  : out STD_LOGIC_VECTOR (N-1 downto 0)
        );
    end component;

    component addsub
        generic (N : integer := 32);
        Port(
            A         : in  STD_LOGIC_VECTOR (N-1 downto 0);
            B         : in  STD_LOGIC_VECTOR (N-1 downto 0);
            nAdd_Sub  : in  STD_LOGIC;
            O         : out STD_LOGIC_VECTOR (N-1 downto 0);
            O_Carry   : out STD_LOGIC
        );
    end component;

begin
    -- Instantiate Register File (mipsreg)
    RegisterFile_inst: mipsreg
        port map (
            clk        => clk,
            i_RST      => i_RST,
            reg_write  => reg_write,
            read_reg1  => read_reg1,
            read_reg2  => read_reg2,
            write_reg  => write_reg,
            write_data => alu_out, -- ALU output feeds into write_data
            read_data1 => reg_read_data1,
            read_data2 => reg_read_data2
        );

    -- Instantiate ALUSrc Mux (mux2t1_N)
    ALUSrc_Mux_inst: mux2t1_N
        generic map (N => 32)
        port map (
            i_S  => ALUSrc,
            i_D0 => reg_read_data2, -- Second register operand
            i_D1 => immediate,      -- Immediate value
            o_O  => mux_ALUSrc_out
        );

    -- Instantiate ALU (addsub)
    ALU_inst: addsub
        generic map (N => 32)
        port map (
            A         => reg_read_data1, -- First register operand
            B         => mux_ALUSrc_out, -- Selected second operand
            nAdd_Sub  => nAdd_Sub,
            O         => alu_out,
            O_Carry   => alu_carry
        );

    -- Connect Outputs
    read_data1 <= reg_read_data1;
    read_data2 <= reg_read_data2;
    alu_result <= alu_out;
end Structural;
