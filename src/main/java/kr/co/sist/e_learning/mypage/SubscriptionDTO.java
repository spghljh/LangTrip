package kr.co.sist.e_learning.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class SubscriptionDTO {
    private Long instructorId;
    private String instructorName;
    private String subscribedAt;
}
