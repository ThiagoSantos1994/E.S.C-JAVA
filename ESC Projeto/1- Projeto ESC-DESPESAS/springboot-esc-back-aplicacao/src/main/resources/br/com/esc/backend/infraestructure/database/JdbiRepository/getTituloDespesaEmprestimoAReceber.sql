SELECT DISTINCT
    ds_TituloEmprestimo
FROM
    tbd_Emprestimos
WHERE
    id_Emprestimo <> 0
    AND tp_EmprestimoAReceber = 'S'
    AND id_Funcionario = :idFuncionario
ORDER BY
    ds_TituloEmprestimo