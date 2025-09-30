library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity SimplifiedMIPSProcessor is
    port (
        clk          : in  std_logic;
        rst          : in  std_logic;
        -- Control Signals
        ALUSrc       : in  std_logic;
        MemRead      : in  std_logic;
        MemWrite     : in  std_logic;
        RegWrite     : in  std_logic;
        -- Register Addresses
        rs, rt, rd   : in  std_logic_vector(4 downto 0);
        -- Immediate value
        imm          : in  std_logic_vector(15 downto 0);
        -- Output Data (for testing)
        alu_result   : out std_logic_vector(31 downto 0);
        mem_data_out : out std_logic_vector(31 downto 0)
    );
end SimplifiedMIPSProcessor;

architecture structural of SimplifiedMIPSProcessor is
    -- Internal Signals
    signal reg_data1, reg_data2, alu_in2, alu_out, mem_data_in : std_logic_vector(31 downto 0);
    signal extended_imm : std_logic_vector(31 downto 0);
    signal mem_data_internal : std_logic_vector(31 downto 0); -- Internal signal for memory output
    signal wd_mux : std_logic_vector(31 downto 0); -- MUX output for Register File write data

    -- Instantiate components
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

    component addsub
        port (
            A         : in  std_logic_vector(31 downto 0);
            B         : in  std_logic_vector(31 downto 0);
            nAdd_Sub  : in  std_logic;
            O         : out std_logic_vector(31 downto 0);
            O_Carry   : out std_logic
        );
    end component;

    component mem
        port (
            clk  : in  std_logic;
            addr : in  std_logic_vector(9 downto 0);
            data : in  std_logic_vector(31 downto 0);
            we   : in  std_logic;
            q    : out std_logic_vector(31 downto 0)
        );
    end component;

    component extender
        port (
            input_16bit  : in  std_logic_vector(15 downto 0);  -- 16-bit input value
            mode         : in  std_logic;                      -- '1' for sign extension, '0' for zero extension
            output_32bit : out std_logic_vector(31 downto 0)   -- 32-bit extended output value
        );
    end component;

begin
    -- Sign/Zero Extender
    ext: extender
        port map (
            input_16bit  => imm,
            mode         => '1',  -- Assuming sign extension
            output_32bit => extended_imm
        );

    -- Register File (using mipsreg)
    reg_file: mipsreg
        port map (
            clk        => clk,
            i_RST      => rst,
            reg_write  => RegWrite,
            read_reg1  => rs,
            read_reg2  => rt,
            write_reg  => rd,
            write_data => wd_mux,       -- Data to write into the register file
            read_data1 => reg_data1,
            read_data2 => reg_data2
        );

    -- ALU Input Selection
    alu_in2 <= reg_data2 when ALUSrc = '0' else extended_imm;

    -- ALU Operation (using addsub)
    alu_inst: addsub
        port map (
            A        => reg_data1,
            B        => alu_in2,
            nAdd_Sub => '0',  -- Assuming addition operation
            O        => alu_out,
            O_Carry  => open  -- Ignoring carry out
        );

    -- Data Memory
    mem_data_in <= reg_data2; -- Data to write to memory comes from reg_data2
    data_mem: mem
        port map (
            clk  => clk,
            addr => alu_out(9 downto 0),  -- Using lower 10 bits of ALU result as address
            data => mem_data_in,
            we   => MemWrite,
            q    => mem_data_internal
        );

    -- MUX for Write Data to Register File
    wd_mux <= mem_data_internal when MemRead = '1' else alu_out;

    -- Outputs
    alu_result   <= alu_out;
    mem_data_out <= mem_data_internal when MemRead = '1' else (others => '0');

end structural;

