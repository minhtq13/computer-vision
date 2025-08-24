import AppButton from "@/components/app-button";
import AppModal from "@/components/app-modal";
import AppSelectSmall from "@/components/app-select-small";
import AppTable from "@/components/app-table";
import AppTooltip from "@/components/app-tooltip";
import SearchFilter from "@/components/search-filter";
import { searchTimeDebounce } from "@/constants";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import useRole from "@/hooks/useRole";
import { useGetParticipantsQuery, useSendEmailResultExamClassMutation } from "@/stores/exam-class/api";
import { RoleBase } from "@/types/enum";
import debounce from "lodash/debounce";
import { useTranslations } from "next-intl";
import { useState } from "react";
import { MdAttachEmail } from "react-icons/md";

interface SuperVisorProps {
  examClassId: any;
  examClassDetail: any;
}

const SuperVisor = ({ examClassId }: SuperVisorProps) => {
  const tCommon = useTranslations("common");
  const tExamClass = useTranslations("examClass");
  const { role } = useRole();
  const [keySearch, setKeySearch] = useState("");
  const [openSendEmailModal, setOpenSendEmailModal] = useState(false);
  const [selectedReceiver, setSelectedReceiver] = useState([]);
  const [additionalReceiver, setAdditionalReceiver] = useState([]);
  const notification = useAppNotification();
  const handleError = useHandleError();

  const [sendEmailResultExamClass, { isLoading: sendEmailLoading }] = useSendEmailResultExamClassMutation();

  const { data: participants } = useGetParticipantsQuery(
    {
      examClassId,
      roleType: "SUPERVISOR",
    },
    {
      refetchOnMountOrArgChange: true,
      skip: !examClassId,
    }
  );

  const onSearch = (value) => {
    setKeySearch(value);
  };
  const handleSearchChangeFilter = debounce((_e) => {
    setKeySearch(_e?.target?.value ?? "");
  }, searchTimeDebounce);

  const handleSendEmail = async () => {
    try {
      if (selectedReceiver.length > 0 || additionalReceiver.length > 0) {
        await sendEmailResultExamClass({
          examClassId,
          toAddresses: selectedReceiver.concat(additionalReceiver),
        });
      }
      notification.success({ description: tExamClass("sendEmailSuccess") });
      setOpenSendEmailModal(false);
    } catch (error) {
      handleError(error);
    }
  };

  const data = participants?.map((item, index) => {
    return {
      key: (index + 1).toString(),
      name: item?.name,
      code: item?.code,
      email: item?.email,
    };
  });
  const columns = [
    {
      title: tCommon("index"),
      dataIndex: "key",
      key: "key",
      width: "5%",
      align: "center",
    },
    {
      title: tCommon("fullName"),
      dataIndex: "name",
      key: "name",
      width: "20%",
      align: "center",
    },
    {
      title: tExamClass("teacherCode"),
      dataIndex: "code",
      key: "code",
      width: "10%",
      align: "center",
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
      width: "20%",
      align: "center",
    },
  ];

  return (
    <div>
      <div className="tab-button mb-3">
        <div className="flex items-center gap-2">
          <SearchFilter placeholder={tExamClass("searchTeacher")} onSearch={onSearch} onChange={handleSearchChangeFilter} />
          {role !== RoleBase.STUDENT && (
            <AppTooltip title={tExamClass("sendEmailTestList")}>
              <AppButton
                type="primary"
                onClick={() => {
                  setOpenSendEmailModal(true);
                }}
              >
                <MdAttachEmail />
              </AppButton>
            </AppTooltip>
          )}
        </div>
        <AppModal
          className="modal-select-email-receiver"
          open={openSendEmailModal}
          onCancel={() => setOpenSendEmailModal(false)}
          title={<p className="text-text-hust font-bold text-[20px]">{tExamClass("selectEmailReceiver")}</p>}
          footer={[
            <AppButton key="back" onClick={() => setOpenSendEmailModal(false)}>
              {tCommon("back")}
            </AppButton>,
            <AppButton
              key="submit"
              type="primary"
              loading={sendEmailLoading}
              disabled={selectedReceiver.length === 0 && additionalReceiver.length === 0}
              onClick={handleSendEmail}
            >
              {tCommon("send")}
            </AppButton>,
          ]}
        >
          <p className="text-text-hust font-bold text-[16px]">{tExamClass("selectEmailReceiverAvailable")}</p>

          <AppSelectSmall
            className="email-select-box"
            allowClear
            mode="multiple"
            placeholder={tExamClass("selectEmailReceiver")}
            options={participants?.map((value) => ({ value: value?.email, label: `${value?.name} - ${value?.email}` }))}
            onChange={(value) => setSelectedReceiver(value)}
          />
          {/*TODO: Handle additional emails*/}
          {/* <p style={{ color: "var(--hust-color)", fontStyle: "italic", marginBottom: 6 }}>Email bổ sung:</p>
    <EmailForm
      onFinish={(values) => setAdditionalReceiver(values?.emails !== undefined ? values?.emails.map((item) => item?.email) : [])}
    /> */}
          <p className="text-text-hust font-bold text-[16px] italic mt-1.5">
            {tExamClass("emailList")}: {selectedReceiver.concat(additionalReceiver).join("; ")}
          </p>
        </AppModal>
      </div>
      <AppTable
        className="exam-class-detail-participants"
        columns={columns as any}
        dataSource={data?.filter((item) => item?.name.toLowerCase().includes(keySearch) || item?.code.toLowerCase().includes(keySearch))}
        loading={false}
        labelPagination={tCommon("teacher")}
      />
    </div>
  );
};

export default SuperVisor;
