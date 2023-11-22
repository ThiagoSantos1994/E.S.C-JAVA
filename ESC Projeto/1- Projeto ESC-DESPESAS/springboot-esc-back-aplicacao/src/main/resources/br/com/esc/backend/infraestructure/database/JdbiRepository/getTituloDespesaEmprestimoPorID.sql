SELECT DISTINCT
    ds_TituloEmprestimo
FROM
    tbd_Emprestimos
WHERE
    id_Emprestimo = :idEmprestimo
    AND id_Funcionario = :idFuncionario
ORDER BY
    ds_TituloEmprestimo