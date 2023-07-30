package org.springybot.dto;

import org.springybot.botModel.Common;
import org.springybot.botModel.enum_.TalentEnum;
import org.springybot.entity.Supply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplyDTO {

    private Long id; // 供應信息ID

    private String userId;

    private String botId;

    private String projectName; // 项目名称

    private String commission; // 佣金奖赏

    private String agency;  // 是否中介

    private String workTime; // 上班时间

    private String guarantee; // 是否支持担保

    private String projectDetail; // 项目内容

    private String flightNumber; // 咨询飞机号

    private String publisher;   //發布人

    private Common common;

    public SupplyDTO(Common common) {
        this.common = common;
    }

    public SupplyDTO(String userId, String botId) {
        this.userId = userId;
        this.botId = botId;
        this.id = null;
        this.projectName= "";
        this.commission = "";
        this.agency = "";
        this.workTime = "";
        this.guarantee = "";
        this.projectDetail = "";
        this.flightNumber = "";
        this.publisher = "";
    }

    public SupplyDTO(String userId, String botId, String projectName, String commission,String agency,
                     String workTime, String guarantee, String projectDetail, String flightNumber, String publisher) {
        this.userId = userId;
        this.botId = botId;
        this.id = null;
        this.projectName = projectName;
        this.commission = commission;
        this.agency = agency;
        this.workTime = workTime;
        this.guarantee = guarantee;
        this.projectDetail = projectDetail;
        this.flightNumber = flightNumber;
        this.publisher = publisher;
    }

    public Supply resetSupplyFields(Supply supply) {
        supply.setProjectName("");
        supply.setCommission("");
        supply.setAgency("");
        supply.setWorkTime("");
        supply.setGuarantee("");
        supply.setProjectDetail("");   
        supply.setFlightNumber("");
        supply.setPublisher("");
        return supply;
    }

    public SupplyDTO createSupplyDTO(String userId, String id) {
        return new SupplyDTO(userId, id, this.projectName, this.commission, this.agency,
                this.workTime, this.guarantee, this.projectDetail, this.flightNumber, this.publisher);
    }

    public SupplyDTO convertToSupplyDTO(Supply supply) {
        return new SupplyDTO(supply.getUserId(), supply.getBotId(), supply.getProjectName(),
                supply.getCommission(), supply.getAgency(), supply.getWorkTime(),
                supply.getGuarantee(), supply.getProjectDetail(), supply.getFlightNumber(), supply.getPublisher());
    }

    public String generateSupplyResponse(Supply supply, Boolean isEdit) {

        this.projectName = Optional.ofNullable(supply.getProjectName()).orElse("");
        this.commission = Optional.ofNullable(supply.getCommission()).orElse("");
        this.agency = Optional.ofNullable(supply.getAgency()).orElse("");
        this.workTime = Optional.ofNullable(supply.getWorkTime()).orElse("");
        this.guarantee = Optional.ofNullable(supply.getGuarantee()).orElse("");
        this.projectDetail = Optional.ofNullable(supply.getProjectDetail()).orElse("");
        this.flightNumber = Optional.ofNullable(supply.getFlightNumber()).orElse("");
        this.publisher = Optional.ofNullable(supply.getPublisher()).orElse("");

        String str = isEdit == true ? "编辑供应" : "供应";

        return str + "\n\n項目名稱：" + projectName + "\n佣金奖赏：" + commission + "\n是否中介："
                + agency + "\n" + "上班时间：" + workTime + "\n" + "是否支持擔保：" + guarantee + "\n"
                + "項目內容：" + projectDetail + "\n✈️咨询飞机号：" + flightNumber;
    }

    public String generateSupplyDetails(Supply supply) {
        StringBuilder sb = new StringBuilder();
        appendIfNotEmpty(sb, "項目名稱：", supply.getProjectName());
        appendIfNotEmpty(sb, "佣金奖赏：", supply.getCommission());
        appendIfNotEmpty(sb, "是否中介：", supply.getAgency());
        appendIfNotEmpty(sb, "上班时间：", supply.getWorkTime());
        appendIfNotEmpty(sb, "是否支持擔保: ", supply.getGuarantee());
        appendIfNotEmpty(sb, "項目內容：", supply.getProjectDetail());
        appendIfNotEmpty(sb, "✈️咨询飞机号：", supply.getFlightNumber());
        appendIfNotEmpty(sb, "发布人：", supply.getPublisher());

        String result = sb.toString().trim();
        return result;
    }

    public String fillSupplyInfo(Supply supply, String[] lines) {
        String returnStr = "";
        for (String line : lines) {
            int colonIndex = line.indexOf("：");
            if (colonIndex >= 0) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();

                String filter = TalentEnum.FIFTY_CHARACTERS_LIMIT.getText();
                value = value.replace(filter, "");
                value = value.substring(0, Math.min(255, value.length()));

                switch (key) {
                    case "項目名稱":
                        supply.setProjectName(value);
                        break;
                    case "佣金奖赏":
                        supply.setCommission(value);
                        break;
                    case "是否中介":
                        supply.setAgency(value);
                        break;
                    case "上班时间":
                        supply.setWorkTime(value);
                        break;
                    case "是否支持擔保":
                        supply.setGuarantee(value);
                        break;
                    case "項目內容":
                        if (value.length() >= 50) {
                            returnStr = "发送失败,項目內容超过50字";
                        }
                        supply.setProjectDetail(value);
                        break;
                    case "✈️咨询飞机号":
                        supply.setFlightNumber(value);
                        break;
                    default:
                        break;
                }
            }
        }
        return returnStr;
    }

    private void appendIfNotEmpty(StringBuilder sb, String label, String value) {
        if (value != null && !value.isEmpty()) {
            sb.append(label).append(value).append("\n");
        }
    }
}
