export const TestFooter = () => {
	const testSig = {
		display: "flex",
		justifyContent: "space-between",
		width: "100%",
		padding: "0px 15px",
		marginBottom: "40px",
	};

	const textNote = {
		paddingLeft: "0px",
		fontStyle: "normal",
		textDecoration: "underline",
	};

	const textNoteP = {
		display: "block",
		paddingLeft: "20px",
		fontStyle: "italic",
	};

	const textBold = {
		fontWeight: "bold",
	};

	const contentStyle = {
		display: "flex",
		flexDirection: "column",
		alignItems: "center",
	};
	return (
		<div className="test-footer" style={contentStyle}>
			<div className="test-end" style={contentStyle}>
				<p>(Cán bộ coi thi không giải thích gì thêm)</p>
				<p className="text-bold" style={textBold}>
					HẾT
				</p>
			</div>
			<div className="test-sig" style={testSig}>
				<div className="sig-left" style={contentStyle}>
					<p className="text-bold" style={textBold}>
						DUYỆT CỦA KHOA/BỘ MÔN
					</p>
					<p>(Ký tên, ghi rõ họ tên)</p>
				</div>
				<div className="sig-right" style={contentStyle}>
					<p>Hà Nội, ngày ..... tháng ..... năm ......</p>
					<p className="text-bold" style={textBold}>
						GIẢNG VIÊN RA ĐỀ
					</p>
					<p>(Ký tên, ghi rõ họ tên)</p>
				</div>
			</div>
			<div className="test-note">
				<p className="text-bold text-note" style={textNote}>
					Lưu ý:
				</p>
				<p style={textNoteP}>{`-	Sử dụng khổ giấy A4;`}</p>
				<p
					style={textNoteP}
				>{`-	Phiếu trả lời trắc nghiệm theo mẫu của TTKT;`}</p>
				<p
					style={textNoteP}
				>{`-	Phải thể hiện số thứ tự trang nếu số trang lớn hơn 1;`}</p>
				<p
					style={textNoteP}
				>{`-	Thí sinh không được sử dụng tài liệu, mọi thắc mắc về đề thi vui lòng hỏi giám thị coi thi.`}</p>
			</div>
		</div>
	);
};
export default TestFooter;
