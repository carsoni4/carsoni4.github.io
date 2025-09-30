library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

--TODO THIS IS JUST A NORMAL REGISTER RIGHT NOW (NEED TO ADAPT FOR PIPELINE)
entity IDEX is
    Port (
        clk        : in  STD_LOGIC;
        i_RST      : in  STD_LOGIC;
        reg_write  : in  STD_LOGIC; --always 1
        write_currentInstruction_data : in STD_LOGIC_VECTOR(31 downto 0);
        write_regout1_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_regout2_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_signextension_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_alucontrol_data : in STD_LOGIC_VECTOR (3 downto 0);
        write_nextAddr_data : in STD_LOGIC_VECTOR(31 downto 0);
        read_currentInstruction_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_regout1_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_regout2_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_signextension_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_alucontrol_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_nextAddr_data : out STD_LOGIC_VECTOR(31 downto 0);
        halt : in std_logic
    );
end IDEX;

architecture Behavioral of IDEX is
    type reg_array is array (0 to 31) of STD_LOGIC_VECTOR (31 downto 0);
    signal registers : reg_array := (others => (others => '0'));
    signal write_regout1_reg : std_logic_vector(4 downto 0) := "00001";
    signal write_regout2_reg : std_logic_vector(4 downto 0) := "00010";
    signal write_signextension_reg : std_logic_vector(4 downto 0) := "00011";
    signal write_alucontrol_reg : STD_LOGIC_VECTOR(4 downto 0) := "00100";
    signal write_currentInstruction_reg : STD_LOGIC_VECTOR(4 downto 0) := "00101";
    signal write_nextAddr_reg : STD_LOGIC_VECTOR(4 downto 0) := "00110";

begin
    process(clk)
    begin
        if rising_edge(clk) then
            if i_RST = '1' then
                -- Reset all registers except $0
                for i in 1 to 31 loop
                    registers(i) <= (others => '0');
                end loop;
            elsif (reg_write = '1' and halt = '0' ) then
                registers(to_integer(unsigned(write_regout1_reg))) <= write_regout1_data;
                registers(to_integer(unsigned(write_regout2_reg))) <= write_regout2_data;
                registers(to_integer(unsigned(write_signextension_reg))) <= write_signextension_data;
                registers(to_integer(unsigned(write_alucontrol_reg))) <= ("0000000000000000000000000000" & write_alucontrol_data);
                registers(to_integer(unsigned(write_currentInstruction_reg))) <=write_currentInstruction_data;
                registers(to_integer(unsigned(write_nextAddr_reg))) <= write_nextAddr_data;
            end if;
        end if;
    end process;

    read_regout1_data <= registers(to_integer(unsigned(write_regout1_reg)));
    read_regout2_data <= registers(to_integer(unsigned(write_regout2_reg)));
    read_signextension_data <= registers(to_integer(unsigned(write_signextension_reg)));
    read_alucontrol_data <= registers(to_integer(unsigned(write_alucontrol_reg)));
    read_currentInstruction_data <= registers(to_integer(unsigned(write_currentInstruction_reg)));
    read_nextAddr_data <= registers(to_integer(unsigned(write_nextAddr_reg)));

end Behavioral;

