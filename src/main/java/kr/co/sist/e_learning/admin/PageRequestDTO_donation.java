package kr.co.sist.e_learning.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequestDTO_donation {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;
    private String orderBy;
    private String sort;

    public int getOffset() {
        return (page - 1) * size;
    }
}
