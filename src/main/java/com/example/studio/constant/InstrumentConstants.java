package com.example.studio.constant;

import java.util.Arrays;
import java.util.List;

public class InstrumentConstants {
    
    public static final List<String> AVAILABLE_INSTRUMENTS = Arrays.asList(
        "Vo.", "Gt.", "Ba.", "Dr.", "Key.", "Per."
    );
    
    private InstrumentConstants() {
        // ユーティリティクラスのため、インスタンス化を防ぐ
    }
}