interface StatCardProps {
  value: string;
  label: string;
  description: string;
}

const StatCard = ({ value, label, description }: StatCardProps) => {
  return (
    <div className="bg-white rounded-xl shadow-md p-6 border border-gray-100">
      <div className="flex flex-col">
        <div className="text-primary text-4xl font-bold mb-2">{value}</div>
        <div className="text-xl font-semibold text-gray-800 mb-2">{label}</div>
        <p className="text-gray-600">{description}</p>
      </div>
    </div>
  );
};

export default StatCard;
