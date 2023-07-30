package org.springybot.dto;

import org.springybot.botModel.Common;
import org.springybot.botModel.enum_.DemandEnum;
import org.springybot.botModel.enum_.TalentEnum;
import org.springybot.entity.Demand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandDTO {

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

    public DemandDTO(Common common) {
        this.common = common;
    }

    public DemandDTO(String userId, String botId) {
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

    public DemandDTO(String userId, String botId, String projectName, String commission, String agency,
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

    public Demand resetDemandFields(Demand demand) {
        demand.setProjectName("");
        demand.setCommission("");
        demand.setAgency("");
        demand.setWorkTime("");
        demand.setGuarantee("");
        demand.setProjectDetail("（限50字以内）");   
        demand.setFlightNumber("");
        demand.setPublisher("");
        return demand;
    }

    public DemandDTO createDemandDTO(String userId, String id) {
        return new DemandDTO(userId, id, this.projectName, this.commission, this.agency,
                this.workTime, this.guarantee, this.projectDetail, this.flightNumber, this.publisher);
    }

    public DemandDTO convertToDemandDTO(Demand demand) {
        return new DemandDTO(demand.getUserId(), demand.getBotId(), demand.getProjectName(),
                demand.getCommission(), demand.getAgency(), demand.getWorkTime(),
                demand.getGuarantee(), demand.getProjectDetail(), demand.getFlightNumber(), demand.getPublisher());
    }

    public String generateDemandResponse(Demand demand, Boolean isEdit) {

        this.projectName = Optional.ofNullable(demand.getProjectName()).orElse("");
        this.commission = Optional.ofNullable(demand.getCommission()).orElse("");
        this.agency = Optional.ofNullable(demand.getAgency()).orElse("");
        this.workTime = Optional.ofNullable(demand.getWorkTime()).orElse("");
        this.guarantee = Optional.ofNullable(demand.getGuarantee()).orElse("");
        this.projectDetail = Optional.ofNullable(demand.getProjectDetail()).orElse(TalentEnum.FIFTY_CHARACTERS_LIMIT.getText());
        this.flightNumber = Optional.ofNullable(demand.getFlightNumber()).orElse("");
        this.publisher = Optional.ofNullable(demand.getPublisher()).orElse("");

        String str = isEdit == true ? "编辑需求" : "需求";

        return str + "\n\n項目名稱：" + projectName + "\n佣金奖赏：" + commission + "\n是否中介："
                + agency + "\n" + "上班时间：" + workTime + "\n" + "是否支持擔保：" + guarantee + "\n"
                + "項目內容：" + projectDetail + "\n✈️咨询飞机号：" + flightNumber;
    }

    public String generateDemandDetails(Demand demand) {
        StringBuilder sb = new StringBuilder();
        appendIfNotEmpty(sb, "項目名稱：", demand.getProjectName());
        appendIfNotEmpty(sb, "佣金奖赏：", demand.getCommission());
        appendIfNotEmpty(sb, "是否中介：", demand.getAgency());
        appendIfNotEmpty(sb, "上班时间：", demand.getWorkTime());
        appendIfNotEmpty(sb, "是否支持擔保: ", demand.getGuarantee());
        appendIfNotEmpty(sb, "項目內容：", demand.getProjectDetail());
        appendIfNotEmpty(sb, "✈️咨询飞机号：", demand.getFlightNumber());
        appendIfNotEmpty(sb, "发布人：", demand.getPublisher());

        String result = sb.toString().trim();
        return result;
    }

    public String fillDemandInfo(Demand demand, String[] lines) {
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
                        demand.setProjectName(value);
                        break;
                    case "佣金奖赏":
                        demand.setCommission(value);
                        break;
                    case "是否中介":
                        demand.setAgency(value);
                        break;
                    case "上班时间":
                        demand.setWorkTime(value);
                        break;
                    case "是否支持擔保":
                        demand.setGuarantee(value);
                        break;
                    case "項目內容":
                        if (value.length() >= 50) {
                            returnStr = DemandEnum.REMIND_PROJECTDETAIL_LIMIT.getText();
                        }
                        demand.setProjectDetail(value);
                        break;
                    case "✈️咨询飞机号":
                        demand.setFlightNumber(value);
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
