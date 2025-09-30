library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity IFID is
    Port (
        clk        : in  STD_LOGIC;
        i_RST      : in  STD_LOGIC;
        reg_write  : in  STD_LOGIC;
        write_adder_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_imem_data : in  STD_LOGIC_VECTOR (31 downto 0);
        read_adder_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_imem_data : out STD_LOGIC_VECTOR (31 downto 0);
        flush : in STD_LOGIC;
        stall : in STD_LOGIC;
        halt : in STD_LOGIC
    );
end IFID;


architecture Behavioral of IFID is
    type reg_array is array (0 to 31) of STD_LOGIC_VECTOR (31 downto 0);
    signal registers : reg_array := (others => (others => '0'));
    signal write_adder_reg : std_logic_vector(4 downto 0) := "00001";
    signal write_imem_reg : std_logic_vector(4 downto 0) := "00010";
begin
    process(clk, i_RST, flush)
    begin
        if rising_edge(clk) then
            if (i_RST = '1' or flush = '1') then
                -- Reset all registers except $0
                registers(to_integer(unsigned(write_adder_reg))) <= (others => '0');
                registers(to_integer(unsigned(write_imem_reg))) <= (others => '0');
            elsif (reg_write = '1' and (halt = '0' and stall = '0')) then
                registers(to_integer(unsigned(write_adder_reg))) <= write_adder_data;
                registers(to_integer(unsigned(write_imem_reg))) <= write_imem_data;
            end if;
        end if;
    end process;

    read_adder_data <= registers(to_integer(unsigned(write_adder_reg)));
    read_imem_data <= registers(to_integer(unsigned(write_imem_reg)));

end Behavioral;
