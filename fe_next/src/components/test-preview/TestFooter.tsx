export const TestFooter = () => {
  return (
    <div className="test-footer flex flex-col items-center">
      <div className="test-end  flex flex-col items-center">
        <p>(Cán bộ coi thi không giải thích gì thêm)</p>
        <p className="font-bold">HẾT</p>
      </div>
      <div className="test-sig flex justify-between w-full py-0 px-4 mb-10">
        <div className="sig-left  flex flex-col items-center">
          <p className="font-bold">DUYỆT CỦA KHOA/BỘ MÔN</p>
          <p>(Ký tên, ghi rõ họ tên)</p>
        </div>
        <div className="sig-right  flex flex-col items-center">
          <p>Hà Nội, ngày ..... tháng ..... năm ......</p>
          <p className="font-bold">GIẢNG VIÊN RA ĐỀ</p>
          <p>(Ký tên, ghi rõ họ tên)</p>
        </div>
      </div>
      <div className="test-note">
        <p className="text-note pl-0 font-normal underline">Lưu ý:</p>
        <p className="block pl-5 underline">{`-	Sử dụng khổ giấy A4;`}</p>
        <p className="block pl-5 underline">{`-	Phiếu trả lời trắc nghiệm theo mẫu của TTKT;`}</p>
        <p className="block pl-5 underline">{`-	Phải thể hiện số thứ tự trang nếu số trang lớn hơn 1;`}</p>
        <p className="block pl-5 underline"> {`-	Thí sinh không được sử dụng tài liệu, mọi thắc mắc về đề thi vui lòng hỏi giám thị coi thi.`}</p>
      </div>
    </div>
  );
};
export default TestFooter;
