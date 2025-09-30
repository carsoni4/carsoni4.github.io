library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity MEMWB is
    Port (
        clk        : in  STD_LOGIC;
        i_RST      : in  STD_LOGIC;
        reg_write  : in  STD_LOGIC;
        write_dmem_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_alu_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_wrMuxO_data : in STD_LOGIC_VECTOR(31 downto 0);
        write_nextAddr_data : in STD_LOGIC_VECTOR(31 downto 0);
        write_halt_data : in STD_LOGIC_VECTOR(31 downto 0);
        read_dmem_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_alu_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_wrMuxO_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_nextAddr_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_halt_data : out STD_LOGIC_VECTOR (31 downto 0);
        halt : in std_logic
    );
end MEMWB;


architecture Behavioral of MEMWB is
    type reg_array is array (0 to 31) of STD_LOGIC_VECTOR (31 downto 0);
    signal registers : reg_array := (others => (others => '0'));
    signal write_dmem_reg : std_logic_vector(4 downto 0) := "00001";
    signal write_alu_reg : std_logic_vector(4 downto 0) := "00010";
    signal write_wrMuxO_reg : STD_LOGIC_VECTOR(4 downto 0) := "00011";
    signal write_nextAddr_reg : STD_LOGIC_VECTOR(4 downto 0) := "00110";
    signal write_halt_reg : STD_LOGIC_VECTOR(4 downto 0) := "00111";
begin
    process(clk)
    begin
        if rising_edge(clk) then
            if i_RST = '1' then
                -- Reset all registers except $0
                for i in 1 to 31 loop
                    registers(i) <= (others => '0');
                end loop;
            elsif (reg_write = '1' and halt = '0')then
                registers(to_integer(unsigned(write_dmem_reg))) <= write_dmem_data;
                registers(to_integer(unsigned(write_alu_reg))) <= write_alu_data;
                registers(to_integer(unsigned(write_wrMuxO_reg))) <= write_wrMuxO_data;
                registers(to_integer(unsigned(write_nextAddr_reg))) <= write_nextAddr_data;
                registers(to_integer(unsigned(write_halt_reg))) <= write_halt_data;
            end if;
        end if;
    end process;

    read_dmem_data <= registers(to_integer(unsigned(write_dmem_reg)));
    read_alu_data <= registers(to_integer(unsigned(write_alu_reg)));
    read_wrMuxO_data <= registers(to_integer(unsigned(write_wrMuxO_reg)));
    read_nextAddr_data <= registers(to_integer(unsigned(write_nextAddr_reg)));
    read_halt_data <= registers(to_integer(unsigned(write_halt_reg)));
end Behavioral;

