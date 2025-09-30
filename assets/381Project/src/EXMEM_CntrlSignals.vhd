library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity EXMEM_CntrlSignals is
    Port (
        clk        : in  STD_LOGIC;
        i_RST      : in  STD_LOGIC;
        reg_write  : in  STD_LOGIC; --always 1 
        jalMux_in : in STD_LOGIC;
        jump_in : in STD_LOGIC;
        memToReg_in : in STD_LOGIC;
        regWrite_in : in STD_LOGIC;
        memRead_in : in STD_LOGIC;
        memWrite_in : in STD_LOGIC;
        branch_in : in STD_LOGIC;
        bSigned_in : in STD_LOGIC;
        jr_in : in STD_LOGIC;
        bne_in : in STD_LOGIC;
        extsigned_in : in STD_LOGIC;
        jump : OUT STD_LOGIC;
        jalMux : OUT STD_LOGIC;
        memToReg : OUT STD_LOGIC;
        regWrite : OUT STD_LOGIC;
        memRead : OUT STD_LOGIC;
        memWrite : OUT STD_LOGIC;
        branch : OUT STD_LOGIC;
        bSigned : OUT STD_LOGIC;
        jr : out STD_LOGIC;
        bne : out STD_LOGIC;
        extsigned : out STD_LOGIC;
        halt  	   : in std_logic
    );
end EXMEM_CntrlSignals;


architecture Behavioral of EXMEM_CntrlSignals is
    type reg_array is array (0 to 31) of STD_LOGIC;
    signal registers : reg_array := (others => '0');
    signal write_jump_reg         : std_logic_vector(4 downto 0) := "00000";
    signal write_ALUsrc_reg       : std_logic_vector(4 downto 0) := "00001";
    signal write_RegDst_reg       : std_logic_vector(4 downto 0) := "00010";
    signal write_memToReg_reg     : std_logic_vector(4 downto 0) := "00011";
    signal write_regWrite_reg     : std_logic_vector(4 downto 0) := "00100";
    signal write_memRead_reg      : std_logic_vector(4 downto 0) := "00101";
    signal write_memWrite_reg     : std_logic_vector(4 downto 0) := "00110";
    signal write_branch_reg       : std_logic_vector(4 downto 0) := "00111";
    signal write_brlShftDir_reg   : std_logic_vector(4 downto 0) := "01000";
    signal write_lui_reg          : std_logic_vector(4 downto 0) := "01001";
    signal write_bSigned_reg      : std_logic_vector(4 downto 0) := "01010";
    signal write_jr_reg           : std_logic_vector(4 downto 0) := "01011";
    signal write_bne_reg          : std_logic_vector(4 downto 0) := "01100";
    signal write_extsigned_reg    : std_logic_vector(4 downto 0) := "01101";
    signal write_jalMux_reg    : std_logic_vector(4 downto 0) := "01110";

begin
    process(clk)
    begin
        if rising_edge(clk) then
            if i_RST = '1' then
                -- Reset all registers except $0
                for i in 1 to 31 loop
                    registers(i) <= '0';
                end loop;
            elsif (reg_write = '1' and  halt = '0') then
                registers(to_integer(unsigned(write_jump_reg)))      <= jump_in;
                registers(to_integer(unsigned(write_memToReg_reg)))  <= memToReg_in;
                registers(to_integer(unsigned(write_memRead_reg)))   <= memRead_in;
                registers(to_integer(unsigned(write_memWrite_reg)))  <= memWrite_in;
                registers(to_integer(unsigned(write_branch_reg)))    <= branch_in;
                registers(to_integer(unsigned(write_bSigned_reg)))   <= bSigned_in;
                registers(to_integer(unsigned(write_jr_reg)))        <= jr_in;
                registers(to_integer(unsigned(write_bne_reg)))       <= bne_in;
                registers(to_integer(unsigned(write_extsigned_reg))) <= extsigned_in;
                registers(to_integer(unsigned(write_jalMux_reg)))    <= jalMux_in;
		        registers(to_integer(unsigned(write_regWrite_reg)))  <= regWrite_in;
            end if;
        end if;
    end process;

    jump       <= registers(to_integer(unsigned(write_jump_reg)));
    memToReg   <= registers(to_integer(unsigned(write_memToReg_reg)));
    regWrite   <= registers(to_integer(unsigned(write_regWrite_reg)));
    memRead    <= registers(to_integer(unsigned(write_memRead_reg)));
    memWrite   <= registers(to_integer(unsigned(write_memWrite_reg)));
    branch     <= registers(to_integer(unsigned(write_branch_reg)));
    bSigned    <= registers(to_integer(unsigned(write_bSigned_reg)));
    jr         <= registers(to_integer(unsigned(write_jr_reg)));
    bne        <= registers(to_integer(unsigned(write_bne_reg)));
    extsigned  <= registers(to_integer(unsigned(write_extsigned_reg)));
    jalMux     <= registers(to_integer(unsigned(write_jalMux_reg)));
end Behavioral;
