export default function Button({
                                   children,
                                   type = 'submit',
                                   onClick,
                                   fullWidth,
                               }) {
    return (
        <button
            type={type}
            onClick={onClick}
            className={`${
                fullWidth ? 'w-full' : ''
            } flex justify-center py-2 px-4 rounded-md text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500`}
        >
            {children}
        </button>
    );
}
