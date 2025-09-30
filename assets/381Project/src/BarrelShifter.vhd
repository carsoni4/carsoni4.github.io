library IEEE;
use IEEE.std_logic_1164.all;

entity barrelShifter is
    port(
        i_X     : in  std_logic_vector(31 downto 0);  
        i_shamt : in  std_logic_vector(4 downto 0);   
        i_type  : in  std_logic;                      -- Shift type (0 log 1 ari
        i_dir   : in  std_logic;                      -- Direction 0 r 1 L
        o_Y     : out std_logic_vector(31 downto 0)   
    );
end barrelShifter;

architecture structural of barrelShifter is
    
    component mux2t1 is
        port(
            i_S  : in  std_logic;
            i_D0 : in  std_logic;
            i_D1 : in  std_logic;
            o_O  : out std_logic
        );
    end component;

   
    type t_stage_array is array (0 to 31) of std_logic_vector(31 downto 0);
    signal stages : t_stage_array;
    
    
    signal input_data      : std_logic_vector(31 downto 0);
    signal reversed_input  : std_logic_vector(31 downto 0);
    signal reversed_output : std_logic_vector(31 downto 0);
    signal fill_value      : std_logic;

begin
    
    
    -- Reverse input 
    INPUT_REVERSE: for i in 0 to 31 generate
        reversed_input(31-i) <= i_X(i);
    end generate;

    
    INPUT_SELECT: for i in 0 to 31 generate 
        INPUT_MUX: mux2t1 port map(
            i_S  => i_dir,
            i_D0 => i_X(i),
            i_D1 => reversed_input(i),
            o_O  => input_data(i)
        );
    end generate;

    -- Determine fill value 
    FILL_SELECT: mux2t1 port map(
        i_S  => i_type,
        i_D0 => '0',
        i_D1 => input_data(31),
        o_O  => fill_value
    );

    
    -- Barrel Shifter Main Stages 
   

    -- Stage 1: 1-bit shift
    STAGE1_MSB: mux2t1 port map(
        i_S  => i_shamt(0),
        i_D0 => input_data(31),
        i_D1 => fill_value,
        o_O  => stages(0)(31)
    );

    STAGE1: for i in 0 to 30 generate
        STAGE1_MUX: mux2t1 port map(
            i_S  => i_shamt(0),
            i_D0 => input_data(i),
            i_D1 => input_data(i+1),
            o_O  => stages(0)(i)
        );
    end generate;

    -- Stage 2: 2-bit shift
    STAGE2_FILL: for i in 30 to 31 generate
        STAGE2_FILL_MUX: mux2t1 port map(
            i_S  => i_shamt(1),
            i_D0 => stages(0)(i),
            i_D1 => fill_value,
            o_O  => stages(1)(i)
        );
    end generate;

    STAGE2_SHIFT: for i in 0 to 29 generate
        STAGE2_MUX: mux2t1 port map(
            i_S  => i_shamt(1),
            i_D0 => stages(0)(i),
            i_D1 => stages(0)(i+2),
            o_O  => stages(1)(i)
        );
    end generate;

    -- Stage 3: 4-bit shift
    STAGE4_FILL: for i in 28 to 31 generate
        STAGE4_FILL_MUX: mux2t1 port map(
            i_S  => i_shamt(2),
            i_D0 => stages(1)(i),
            i_D1 => fill_value,
            o_O  => stages(2)(i)
        );
    end generate;

    STAGE4_SHIFT: for i in 0 to 27 generate
        STAGE4_MUX: mux2t1 port map(
            i_S  => i_shamt(2),
            i_D0 => stages(1)(i),
            i_D1 => stages(1)(i+4),
            o_O  => stages(2)(i)
        );
    end generate;

    -- Stage 4: 8-bit shift
    STAGE8_FILL: for i in 24 to 31 generate
        STAGE8_FILL_MUX: mux2t1 port map(
            i_S  => i_shamt(3),
            i_D0 => stages(2)(i),
            i_D1 => fill_value,
            o_O  => stages(3)(i)
        );
    end generate;

    STAGE8_SHIFT: for i in 0 to 23 generate
        STAGE8_MUX: mux2t1 port map(
            i_S  => i_shamt(3),
            i_D0 => stages(2)(i),
            i_D1 => stages(2)(i+8),
            o_O  => stages(3)(i)
        );
    end generate;

    -- Stage 5: 16-bit shift
    STAGE16_FILL: for i in 16 to 31 generate
        STAGE16_FILL_MUX: mux2t1 port map(
            i_S  => i_shamt(4),
            i_D0 => stages(3)(i),
            i_D1 => fill_value,
            o_O  => stages(4)(i)
        );
    end generate;

    STAGE16_SHIFT: for i in 0 to 15 generate
        STAGE16_MUX: mux2t1 port map(
            i_S  => i_shamt(4),
            i_D0 => stages(3)(i),
            i_D1 => stages(3)(i+16),
            o_O  => stages(4)(i)
        );
    end generate;

 
    
    -- Reverse the final output
    OUTPUT_REVERSE: for i in 0 to 31 generate
        reversed_output(31-i) <= stages(4)(i);
    end generate;

    -- Select between normal and reversed output
    OUTPUT_SELECT: for i in 0 to 31 generate
        OUTPUT_MUX: mux2t1 port map(
            i_S  => i_dir,
            i_D0 => stages(4)(i),
            i_D1 => reversed_output(i),
            o_O  => o_Y(i)
        );
    end generate;

end structural;
-- implemented left shift as a right shift but just reversing when needed
