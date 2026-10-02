package com.ksh.dto;

import lombok.Data;

// 프론트엔드에서 캠페인을 등록할 때 서버로 전송할 데이터
@Data
public class AdoptionCampaignRequest {
  private String hashtag, title, content, thumbnailUrl, mediaType, mediaUrl;
}
