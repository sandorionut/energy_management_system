export default function Input({
                                  id,
                                  label,
                                  type = 'text',
                                  value,
                                  onChange,
                                  name,
                              }) {
    return (
        <div>
            <label htmlFor={id} className="block text-sm font-medium text-gray-700">
                {label}
            </label>
            <div className="mt-1">
                <input
                    id={id}
                    name={name || id}
                    type={type}
                    required
                    value={value}
                    onChange={onChange}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                />
            </div>
        </div>
    );
}
